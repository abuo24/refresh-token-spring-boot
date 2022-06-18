package uz.zako.lesson62.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.util.UriEncoder;
import uz.zako.lesson62.entity.Attachment;
import uz.zako.lesson62.model.Result;
import uz.zako.lesson62.repository.AttachmentRepository;
import uz.zako.lesson62.service.AttachmentService;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j // log uchun
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    @Value("${file.upload.folder}")
    private String uploadPath;
    private final AttachmentRepository attachmentRepository;

    private String resource = "Attachment ";

//    @PostConstruct
//    void init() {
//        log.info(uploadPath);
//        if (!uploadPath.equals("upload")) {
//            uploadPath = "upload";
//        }
//        log.info(uploadPath);
//    }

    @Override
    public ResponseEntity<?> upload(MultipartFile multipartFile) {
        try {
            Attachment attachment = new Attachment();
            attachment.setContentType(multipartFile.getContentType());
            attachment.setName(multipartFile.getOriginalFilename());
            attachment.setFileSize(multipartFile.getSize() / 8 / 1024);
            attachment.setExtension(getExtension(multipartFile.getOriginalFilename()));
            attachment.setHashId(UUID.randomUUID().toString());
            String uploadFolder = String.format("%s/%d/%d/%d/%s.%s", uploadPath,
                    1900 + new Date().getYear(),
                    1 + new Date().getMonth(),
                    new Date().getDate(),
                    attachment.getHashId(),
                    attachment.getExtension()
            );
            attachment.setUploadPath(uploadFolder);
            File catalog = new File(uploadFolder);
            if (!catalog.exists()) {
                catalog.mkdirs();
            }
            attachmentRepository.save(attachment);

            multipartFile.transferTo(catalog.getAbsoluteFile());
            return ResponseEntity.ok(Result.ok(null));
        } catch (Exception e) {
            log.error("error save attachment - {}", e.getMessage());
            return new ResponseEntity(Result.error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getExtension(String fileName) {
        String extension = null;
        if (fileName != null && !fileName.isEmpty()) {
//            dsaghjhsdj.a.jpg
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return extension;
    }

    @Override
    public ResponseEntity<?> preview(String hashId) {
        try {
            Optional<Attachment> attachment = attachmentRepository.findByHashId(hashId);
            if (!attachment.isPresent()) {
                return new ResponseEntity(Result.error(String.format("attachment not found hashId - %s", hashId)), HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok().header("contentDisposition=inline&fileName=" + attachment.get().getName()).contentType(MediaType.parseMediaType(attachment.get().getContentType())).body(
                    new FileUrlResource(String.format("%s",
                            attachment.get().getUploadPath())));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new ResponseEntity(Result.error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> download(String hashId) {
        try {
            Optional<Attachment> attachment = attachmentRepository.findByHashId(hashId);
            if (!attachment.isPresent()) {
                return new ResponseEntity(Result.error(String.format("attachment not found hashId - %s", hashId)), HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok().header("contentDisposition=attachment&fileName=" + UriEncoder.decode(attachment.get().getName())).contentType(MediaType.parseMediaType(attachment.get().getContentType())).body(
                    new FileUrlResource(String.format("%s",
                            attachment.get().getUploadPath())));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new ResponseEntity(Result.error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> delete(String hashId) {

        try {

            Attachment attachment = attachmentRepository.findByHashId(hashId).orElseThrow(() -> new RuntimeException(resource + "not found hashId: " + hashId));
            File file = new File(attachment.getUploadPath());
            if (file.exists()) {
                file.delete();
            }
//            attachmentRepository.delete(attachment);
//            attachmentRepository.deleteById(attachment.getId());
            attachmentRepository.deleteByHashId(attachment.getHashId());
            return ResponseEntity.ok(Result.ok(null));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(Result.error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}

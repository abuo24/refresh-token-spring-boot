package uz.zako.lesson62.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

    ResponseEntity<?> upload(MultipartFile multipartFile);

    ResponseEntity<?> preview(String hashId);

    ResponseEntity<?> download(String hashId);

    ResponseEntity<?> delete(String hashId);
}

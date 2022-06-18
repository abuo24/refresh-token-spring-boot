package uz.zako.lesson62.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.zako.lesson62.service.AttachmentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        return attachmentService.upload(file);
    }

    @GetMapping("/{hashId}")
    public ResponseEntity<?> preview(@PathVariable("hashId") String hashId) {
        return attachmentService.preview(hashId);
    }

    @GetMapping("/download/{hashId}")
    public ResponseEntity<?> download(@PathVariable("hashId") String hashId) {
        return attachmentService.download(hashId);
    }

    @DeleteMapping("/{hashId}")
    public ResponseEntity<?> delete(@PathVariable("hashId") String hashId) {
        return attachmentService.delete(hashId);
    }

}

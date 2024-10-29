package vn.job.controller;


import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.job.dto.response.error.ResponseData;
import vn.job.dto.response.error.ResponseError;
import vn.job.service.EmailService;

import java.io.UnsupportedEncodingException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send-mail")
    public ResponseData<String> sendEmail(@RequestParam String recipients, @RequestParam String subject,
                            @RequestParam String content, @RequestParam(required = false) MultipartFile[] files) {
        log.info("Sending email to: {}", recipients);
        try {
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), emailService.sendEmail(recipients, subject, content, files));
        } catch (MessagingException  | UnsupportedEncodingException e) {
            log.error("Sending email was failure, message={}", e.getMessage(), e);
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Sending email was failure");
        }
    }
}

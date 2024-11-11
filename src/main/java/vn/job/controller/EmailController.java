package vn.job.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import vn.job.service.SubscriberService;

import java.io.UnsupportedEncodingException;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Email Controller")
public class EmailController {
    private final EmailService emailService;

    private final SubscriberService subscriberService;

    @PostMapping("/send-mail")
    @Operation(summary = "Send mail", description = "API for send mail")
    public ResponseData<String> sendEmail() {
        log.info("Sending email to");
        try {
            this.subscriberService.sendSubscribersEmailJobs();
            return new ResponseData<>(HttpStatus.ACCEPTED.value(),"Sending email success" );
        } catch (Exception e) {
            log.error("Sending email was failure, message={}", e.getMessage(), e);
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Sending email was failure");
        }
    }
}

package vn.job.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import vn.job.model.Job;
import vn.job.repository.JobRepository;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j

public class EmailService {
    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    private final JobRepository jobRepository;

    @Value("${spring.mail.from}")
    private String emailFrom;


    @Async
    public void sendConfirmLink(@Email(message = "email invalid format") String emailTo, Long id, String secretCode) throws MessagingException, UnsupportedEncodingException {
        log.info("Send email confirm account");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        Context context =   new Context();
        String linkConfirm = String.format("http://localhost:8080/user/confirm/%s?secretCode=%s", id, secretCode);
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);
        helper.setFrom(emailFrom,"Văn Nhân");
        helper.setSubject("Please confirm your account");
        helper.setTo(emailTo);
        String html = this.templateEngine.process("confirm-email.html", context);
        helper.setText(html, true);
        this.mailSender.send(message);
        log.info("Confirm account successfully to {}", emailTo);
    }

    @Async
    public void sendResetPasswordLink( String emailTo   , String secretCode) throws MessagingException, UnsupportedEncodingException {
        log.info("Sending reset password email to {}", emailTo);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context =   new Context();

        String resetPasswordLink = String.format("http://localhost:8080/auth/reset-password?secretKey=%s", secretCode);
        Map<String, Object> properties = new HashMap<>();
        properties.put("resetPasswordLink", resetPasswordLink);
        context.setVariables(properties);
        helper.setFrom(emailFrom,"Văn Nhân");
        helper.setSubject("Please confirm reset password");
        helper.setTo(emailTo);
        String html = this.templateEngine.process("reset-password-email.html", context);
        helper.setText(html, true);
        this.mailSender.send(message);
        log.info("Reset password email sent successfully to {}", emailTo);
    }


    @Async
    public void sendSubscriberJob(String emailTo, String username, Object arrJobs) throws MessagingException, UnsupportedEncodingException {
        log.info("Sending subscriber job {}", emailTo);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context =   new Context();

        Map<String, Object> properties = new HashMap<>();
        properties.put("name", username);
        properties.put("jobs", arrJobs);
        context.setVariables(properties);
        helper.setFrom(emailFrom,"Văn Nhân");
        helper.setSubject("Hãy khám phá ngay cơ hội việc làm!");
        helper.setTo(emailTo);
        String html = this.templateEngine.process("job-email.html", context);
        helper.setText(html, true);
        this.mailSender.send(message);
        log.info("Subscriber job successfully to {}", emailTo);
    }
}


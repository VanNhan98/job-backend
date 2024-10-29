package vn.job.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.from}")
    private String emailFrom;

    public String sendEmail(String recipients, String subject, String content, MultipartFile[] files) throws MessagingException, UnsupportedEncodingException {

        log.info("Sending.....");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(emailFrom, "Văn Nhân");

        if (recipients.contains(",")) { // send to multiple users
            helper.setTo(InternetAddress.parse(recipients));
        } else { // send to single user
            helper.setTo(recipients);
        }

        // Send attach files
        if (files != null) {
            for (MultipartFile file : files) {
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }
        }

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);

        log.info("Email has sent to successfully, recipients: {}", recipients);

        return "Email sent successfully";
    }

    public void sendConfirmLink(@Email(message = "email invalid format") String emailTo, Long id, String secretCode) throws MessagingException, UnsupportedEncodingException {
        log.info("Send email confirm account");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        Context context =   new Context();
        String linkConfirm = "user/id/secretCode";
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);
        helper.setFrom(emailFrom,"Văn Nhân");
        helper.setSubject("Please confirm your account");
        helper.setTo(emailTo);
        String html = this.templateEngine.process("confirm-email.html", context);
        helper.setText(html, true);
        this.mailSender.send(message);
    }
}


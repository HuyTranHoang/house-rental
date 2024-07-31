package com.project.house.rental.service.email;

import com.project.house.rental.dto.email.ContactDto;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailSenderServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("huy.th878@aptechlearning.edu.vn");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    @Override
    @Async
    public void sendRegisterHTMLMail(String to) {
        try {
            Context context = new Context();
            context.setVariable("username", to);
            String text = templateEngine.process("register-email-template", context);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setSubject("Chào mừng bạn đến với Mogu");
            helper.setFrom("huy.th878@aptechlearning.edu.vn");
            helper.setTo(to);
            helper.setText(text, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    @Override
    @Async
    public void sendContactHTMLMail(ContactDto contactDto) {
        try {
            String name = contactDto.getName();
            String email = contactDto.getEmail();
            String message = contactDto.getMessage();

            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("email", email);
            context.setVariable("message", message);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", new Locale("vi", "VN"));
            String formattedDate = dateFormat.format(new Date());
            context.setVariable("sendDate", formattedDate);

            String text = templateEngine.process("contact-email-template", context);
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            helper.setPriority(1);
            helper.setSubject("Mogu - Liên hệ từ " + name);
            helper.setFrom(email);
            helper.setTo("huy.th878@aptechlearning.edu.vn");
            helper.setText(text, true);
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    @Override
    public void sendResetPasswordHTMLMail(String to, String token) {
        try {
            Context context = new Context();
            context.setVariable("email", to);
            context.setVariable("token", token);
            String text = templateEngine.process("reset-password-email-template", context);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setSubject("Mogu - Đặt lại mật khẩu");
            helper.setFrom("huy.th878@aptechlearning.edu.vn");
            helper.setTo(to);
            helper.setText(text, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    @Override
    public void sendReportHTMLMail(String to, String username, String propertyTitle) {
        try {
            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("propertyTitle", propertyTitle);
            String text = templateEngine.process("report-email-template", context);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setSubject("Mogu - Báo cáo bài đăng");
            helper.setFrom("huy.th878@aptechlearning.edu.vn");
            helper.setTo(to);
            helper.setText(text, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }
}

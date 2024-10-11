package com.project.house.rental.service.email;

import com.project.house.rental.dto.email.ContactDto;
import jakarta.mail.MessagingException;
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
            createMessage(to, text, "Mogu - Đăng ký tài khoản");
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
    @Async
    public void sendResetPasswordHTMLMail(String to, String token) {
        try {
            Context context = new Context();
            context.setVariable("email", to);
            context.setVariable("token", token);
            String text = templateEngine.process("reset-password-email-template", context);
            createMessage(to, text, "Mogu - Đặt lại mật khẩu");
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    @Override
    @Async
    public void sendReportHTMLMail(String to, String username, String propertyTitle) {
        try {
            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("propertyTitle", propertyTitle);
            String text = templateEngine.process("report-email-template", context);
            createMessage(to, text, "Mogu - Báo cáo bài đăng");
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    @Override
    @Async
    public void sendCommentReportHTMLMail(String to, String username, String propertyTitle) {
        try {
            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("propertyTitle", propertyTitle);
            String text = templateEngine.process("comment-report-email-template", context);
            createMessage(to, text, "Mogu - Báo cáo bình luận");
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    @Override
    @Async
    public void sendBlockHTMLMail(String to, String username, String propertyTitle) {
        try {
            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("propertyTitle", propertyTitle);
            String text = templateEngine.process("block-email-template", context);
            createMessage(to, text, "Mogu - Thông báo khóa bài đăng");
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    @Override
    @Async
    public void sendBlockCommentHTMLMail(String to, String username, String comment) {
        try {
            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("comment", comment);
            String text = templateEngine.process("block-comment-email-template", context);
            createMessage(to, text, "Mogu - Thông báo khóa bình luận");
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    @Override
    @Async
    public void sendUnblockHTMLMail(String to, String username,String propertyTitle) {
        try {
            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("propertyTitle", propertyTitle);
            String text = templateEngine.process("unblock-email-template", context);
            createMessage(to, text, "Mogu - Thông báo mở khóa bài đăng");
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    @Override
    @Async
    public void sendRechargeHTMLMail(String to, String username, String amount) {
        try {
            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("amount", amount);
            String text = templateEngine.process("recharge-email-template", context);
            createMessage(to, text, "Mogu - Thông báo nạp tiền thành công");
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    @Override
    @Async
    public void sendRejectHTMLMail(String to, String username, String propertyTitle, String reason) {
        try {
            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("propertyTitle", propertyTitle);
            context.setVariable("reason", reason);
            String text = templateEngine.process("reject-email-template", context);
            createMessage(to, text, "Mogu - Thông báo từ chối duyệt bài đăng");
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    private void createMessage(String to, String text, String subject) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setPriority(1);
        helper.setSubject(subject);
        helper.setFrom("huy.th878@aptechlearning.edu.vn");
        helper.setTo(to);
        helper.setText(text, true);
        javaMailSender.send(message);
    }
}

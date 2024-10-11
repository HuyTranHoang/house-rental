package com.project.house.rental.service.email;

import com.project.house.rental.dto.email.ContactDto;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String text);

    void sendRegisterHTMLMail(String to);

    void sendContactHTMLMail(ContactDto contactDto);

    void sendResetPasswordHTMLMail(String to, String token);

    void sendReportHTMLMail(String to, String username, String propertyTitle);

    void sendCommentReportHTMLMail(String to, String username, String comment);

    void sendBlockHTMLMail(String to, String username, String propertyTitle);

    void sendBlockCommentHTMLMail(String to, String username, String comment);

    void sendUnblockHTMLMail(String to, String username, String propertyTitle);

    void sendRechargeHTMLMail(String to, String username, String amount);

    void sendRejectHTMLMail(String to, String username, String propertyTitle, String reason);

    void sendApproveHTMLMail(String to, String username, String propertyTitle);
}

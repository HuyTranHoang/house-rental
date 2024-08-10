package com.project.house.rental.service.email;

import com.project.house.rental.dto.email.ContactDto;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String text);

    void sendRegisterHTMLMail(String to);

    void sendContactHTMLMail(ContactDto contactDto);

    void sendResetPasswordHTMLMail(String to, String token);

    void sendReportHTMLMail(String to, String username, String propertyTitle);

    void sendBlockHTMLMail(String to, String username, String propertyTitle);

}

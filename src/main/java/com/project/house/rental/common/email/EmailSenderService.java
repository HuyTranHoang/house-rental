package com.project.house.rental.common.email;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String text);

}

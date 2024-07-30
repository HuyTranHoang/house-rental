package com.project.house.rental.controller;

import com.project.house.rental.dto.email.ContactDto;
import com.project.house.rental.service.email.EmailSenderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final EmailSenderService emailSenderService;

    public ContactController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping
    public ResponseEntity<String> sendContactEmail(@RequestBody @Valid ContactDto contactDto) {
        emailSenderService.sendContactHTMLMail(contactDto);
        return ResponseEntity.ok("Email sent successfully");
    }
}

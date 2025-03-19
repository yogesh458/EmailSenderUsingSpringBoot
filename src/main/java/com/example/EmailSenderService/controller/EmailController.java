package com.example.EmailSenderService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.EmailSenderService.service.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestParam String to, 
                            @RequestParam String subject, 
                            @RequestParam String body) {
        return emailService.sendEmail(to, subject, body);
    }

    @PostMapping("/send-html")
    public String sendEmail(@RequestParam String to, 
                            @RequestParam String subject) {
        return emailService.sendHtmlEmail(to, subject);
    }

    @PostMapping("/send-pdf")
    public String sendEmail(@RequestParam String email, 
                            @RequestParam String name, 
                            @RequestParam String accountNo, 
                            @RequestParam String atmPin, 
                            @RequestParam String dob) {
        try {
            emailService.sendEmailWithPdf(email, name, accountNo, atmPin, dob);
            return "Email sent successfully to " + email;
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}

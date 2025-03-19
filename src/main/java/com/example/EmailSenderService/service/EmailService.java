package com.example.EmailSenderService.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

     @Autowired
    private PdfService pdfService;

    public String sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("yogeshcshivu@gmail.com"); // Sender Email

            mailSender.send(message);
            return "Email Sent Successfully!";
        } catch (Exception e) {
            return "Error while sending email: " + e.getMessage();
        }
    }


    public String sendHtmlEmail(String to, String subject) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("yogeshcshivu@gmail.com"); // Sender Email

            // ✅ HTML Email Body
            String htmlContent = """
                <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
                        .container { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }
                        h2 { color: #007bff; }
                        p { font-size: 14px; }
                        .footer { margin-top: 20px; font-size: 12px; color: gray; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h2>Welcome to Our Service!</h2>
                        <p>Hello <b>John Doe</b>,</p>
                        <p>We are excited to have you on board. Below are your account details:</p>
                        <p><b>Account No:</b> 123456789</p>
                        <p><b>ATM PIN:</b> 9876</p>
                        <p><b>Login ID:</b> johndoe@example.com</p>
                        <p>If you have any questions, feel free to contact us.</p>
                        <p class="footer">Best Regards, <br> Your Company Name</p>
                    </div>
                </body>
                </html>
                """;

            helper.setText(htmlContent, true); // Set HTML content
            mailSender.send(message);
            
            return "HTML Email Sent Successfully!";
        } catch (MessagingException e) {
            return "Error while sending email: " + e.getMessage();
        }
    }

     public void sendEmailWithPdf(String recipientEmail, String recipientName, String accountNo, String atmPin, String dob) throws Exception {
        File pdfFile = pdfService.generatePasswordProtectedPdf(recipientName, accountNo, atmPin, dob);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipientEmail);
        helper.setSubject("Your Password-Protected Banking Document");
        helper.setText("Hello " + recipientName + ",\n\n"
                + "Please find the attached confidential banking document.\n"
                + "Use your date of birth (YYYYMMDD) as the password to open the PDF.");

        FileSystemResource file = new FileSystemResource(pdfFile);
        helper.addAttachment("BankingDetails.pdf", file);

        mailSender.send(message);
    }
}

package com.example.EmailSenderService.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdfService {

    public File generatePasswordProtectedPdf(String recipientName, String accountNo, String atmPin, String dob) throws IOException {
        String filePath = "protected.pdf";
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Confidential Banking Document");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 14);
            contentStream.newLineAtOffset(100, 650);
            contentStream.showText("Name: " + recipientName);
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(100, 620);
            contentStream.showText("Account Number: " + accountNo);
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(100, 590);
            contentStream.showText("ATM PIN: " + atmPin);
            contentStream.endText();
        }

        // ✅ Set access permissions (allowing only viewing, not editing)
        AccessPermission accessPermission = new AccessPermission();
        accessPermission.setCanModify(false);  // Prevent modifications
        accessPermission.setCanPrint(false);   // Prevent printing

        // ✅ Apply password protection
        StandardProtectionPolicy policy = new StandardProtectionPolicy(dob, dob, accessPermission);
        policy.setEncryptionKeyLength(128); // 128-bit encryption

        document.protect(policy);
        document.save(filePath);
        document.close();

        return new File(filePath);
    }
}



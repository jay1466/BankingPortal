package com.webapp.bankingportal.service;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public CompletableFuture<Void> sendEmail(String to, String subject, String text) {
        val future = new CompletableFuture<Void>();

        try {
            val message = mailSender.createMimeMessage();
            val helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            // From address is automatically set by Spring Boot based on your properties
            helper.setSubject(subject);
            helper.setText(text, true); // Set the second parameter to true to send HTML content
            mailSender.send(message);

            log.info("Sent email to {}", to);
            future.complete(null);

        } catch (MessagingException | MailException e) {
            log.error("Failed to send email to {}", to, e);
            future.completeExceptionally(e);
        }

        return future;
    }

    @Override
    public String getLoginEmailTemplate(String name, String loginTime, String loginLocation) {
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width: 320px; max-width: 1000px; margin: 0 auto; overflow: auto; line-height: 2; background-color: #f1f1f1; padding: 20px;\">"
                + "<div style=\"margin: 50px auto; width: 100%; max-width: 600px; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\">"
                + "<div style=\"border-bottom: 1px solid #ddd; padding-bottom: 10px; text-align: center;\">"
                + "<a href=\"https://onestopbank.netlify.app/\" style=\"text-decoration: none;\">"
                + "<img src=\"https://onestopbank.netlify.app/assets/onestoplogo.jpg\" alt=\"OneStopBank\" style=\"height: 50px; margin-bottom: 10px;\">"
                + "</a>" + "<h1 style=\"font-size: 1.8em; color: #3f51b5; margin: 10px 0;\">OneStopBank</h1>" + "</div>"
                + "<div style=\"padding: 20px;\">" + "<p style=\"font-size: 1.2em; color: #333;\">Hi, " + name + ",</p>"
                + "<p style=\"font-size: 1em; color: #333;\">A login attempt was made on your account at:</p>"
                + "<p style=\"font-size: 1em; color: #555;\">Time: <strong style=\"color: #3f51b5;\">" + loginTime
                + "</strong></p>"
                + "<p style=\"font-size: 1em; color: #555;\">Location: <strong style=\"color: #3f51b5;\">"
                + loginLocation + "</strong></p>"
                + "<p style=\"font-size: 1em; color: #333;\">If this was you, no further action is required. If you suspect any unauthorized access, please change your password immediately and contact our support team.</p>"
                + "<p style=\"font-size: 1em; color: #555;\">Regards,<br />The OneStopBank Team</p>" + "</div>"
                + "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\" />"
                + "<div style=\"text-align: center; font-size: 0.9em; color: #888;\">"
                + "<p>Need help? Contact our support team:</p>"
                + "<p>Email: <a href=\"mailto:onestopbank@google.com\" style=\"color: #3f51b5; text-decoration: none;\">onestopbank@google.com</a></p>"
                + "<div style=\"margin-top: 20px;\">"
                + "<p style=\"font-size: 1em; color: #333;\">Show your support here ❤️</p>"
                + "</div>" + "</div>" + "</div>" + "</div>";
    }

    @Override
    public String getOtpLoginEmailTemplate(String name, String accountNumber, String otp) {
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width: 320px; max-width: 1000px; margin: 0 auto; overflow: auto; line-height: 2; background-color: #f1f1f1; padding: 20px;\">"
                + "<div style=\"margin: 50px auto; width: 100%; max-width: 600px; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\">"
                + "<div style=\"border-bottom: 1px solid #ddd; padding-bottom: 10px; text-align: center;\">"
                + "<a href=\"https://onestopbank.netlify.app/\" style=\"text-decoration: none;\">"
                + "<img src=\"https://onestopbank.netlify.app/assets/onestoplogo.jpg\" alt=\"OneStopBank\" style=\"height: 50px; margin-bottom: 10px;\">"
                + "</a>" + "<h1 style=\"font-size: 1.8em; color: #3f51b5; margin: 10px 0;\">OneStopBank</h1>" + "</div>"
                + "<div style=\"padding: 20px;\">" + "<p style=\"font-size: 1.2em; color: #333;\">Hi, " + name + ",</p>"
                + "<p style=\"font-size: 1em; color: #555;\">Account Number: <strong style=\"color: #3f51b5;\">"
                + accountNumber + "</strong></p>"
                + "<p style=\"font-size: 1em; color: #333;\">Thank you for choosing OneStopBank. Use the following OTP to complete your login procedures. The OTP is valid for "
                + OtpServiceImpl.OTP_EXPIRY_MINUTES + " minutes:</p>"
                + "<h2 style=\"background: #3f51b5; margin: 20px 0; width: max-content; padding: 10px 20px; color: #fff; border-radius: 4px;\">"
                + otp + "</h2>" + "<p style=\"font-size: 1em; color: #555;\">Regards,<br />The OneStopBank Team</p>"
                + "</div>" + "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\" />"
                + "<div style=\"text-align: center; font-size: 0.9em; color: #888;\">"
                + "<p>Need help? Contact our support team:</p>"
                + "<p>Email: <a href=\"mailto:onestopbank@google.com\" style=\"color: #3f51b5; text-decoration: none;\">onestopbank@google.com</a></p>"
                + "<div style=\"margin-top: 20px;\">"
                + "<p style=\"font-size: 1em; color: #333;\">Show your support here ❤️</p>"
                + "</div>" + "</div>" + "</div>" + "</div>";
    }

    @Override
    public String getBankStatementEmailTemplate(String name, String statementText) {
         return "<div style=\"font-family: Arial, sans-serif; padding: 20px;\">" +
                "<h2>Bank Statement</h2>" +
                "<p>Dear " + name + ",</p>" +
                "<p>Here is your latest bank statement:</p>" +
                "<pre style=\"background: #f4f4f4; padding: 10px; border-radius: 5px;\">" +
                statementText +
                "</pre>" +
                "<p>Regards,<br/>OneStopBank Team</p>" +
                "</div>";
    }

    @Override
    public String getTransactionAlertEmailTemplate(String name, String accountNumber, String transactionType, String date, String info, double amount) {
        String maskedAccount = "XXXXXX" + (accountNumber.length() >= 4 ? accountNumber.substring(accountNumber.length() - 4) : accountNumber);
        return "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #ddd; padding: 20px;\">" +
                "<div style=\"text-align: center; border-bottom: 2px solid #3f51b5; padding-bottom: 10px; margin-bottom: 20px;\">" +
                "<h2 style=\"color: #3f51b5; margin: 0;\">OneStopBank</h2>" +
                "<p style=\"margin: 5px 0; color: #555;\">Transaction Alert - Digital Banking Notification</p>" +
                "</div>" +
                "<div style=\"text-align: center; background-color: #4CAF50; color: white; padding: 10px; font-weight: bold; font-size: 18px; margin-bottom: 20px;\">" +
                "SUCCESS" +
                "</div>" +
                "<p>Dear <strong>" + name + "</strong>, a transaction has been successfully processed from your account.</p>" +
                "<table style=\"width: 100%; border-collapse: collapse; margin-top: 20px;\">" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; color: #555; width: 40%;\">Account Number</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; font-weight: bold;\">" + maskedAccount + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; color: #555;\">Transaction Type</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; font-weight: bold;\">" + transactionType + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; color: #555;\">Date</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; font-weight: bold;\">" + date + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; color: #555;\">Tran Info</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; font-weight: bold;\">" + info + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; color: #555;\">Amount</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; font-weight: bold; color: #d32f2f;\">Rs. " + String.format("%.2f", amount) + "</td>" +
                "</tr>" +
                "</table>" +
                "<div style=\"margin-top: 30px; font-size: 12px; color: #888; text-align: center;\">" +
                "<p>If you did not authorize this transaction, please contact our support team immediately.</p>" +
                "<p>Regards,<br/>The OneStopBank Team</p>" +
                "</div>" +
                "</div>";
    }

    @Override
    public String getWelcomeEmailTemplate(String name, String accountNumber, String branch, String ifsc) {
        return "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #ddd; padding: 20px;\">" +
                "<div style=\"text-align: center; border-bottom: 2px solid #3f51b5; padding-bottom: 10px; margin-bottom: 20px;\">" +
                "<h2 style=\"color: #3f51b5; margin: 0;\">OneStopBank</h2>" +
                "<p style=\"margin: 5px 0; color: #555;\">Welcome to Digital Banking!</p>" +
                "</div>" +
                "<p>Dear <strong>" + name + "</strong>,</p>" +
                "<p>Your account has been successfully created. Here are your account details:</p>" +
                "<table style=\"width: 100%; border-collapse: collapse; margin-top: 20px;\">" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; color: #555; width: 40%;\">Account Number</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; font-weight: bold; color: #3f51b5;\">" + accountNumber + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; color: #555;\">Branch</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; font-weight: bold;\">" + branch + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; color: #555;\">IFSC Code</td>" +
                "<td style=\"padding: 10px; border-bottom: 1px solid #eee; font-weight: bold;\">" + ifsc + "</td>" +
                "</tr>" +
                "</table>" +
                "<p style=\"margin-top: 20px;\">Please log in to your account to create a secure 4-digit PIN before attempting any transactions.</p>" +
                "<div style=\"margin-top: 30px; font-size: 12px; color: #888; text-align: center;\">" +
                "<p>Regards,<br/>The OneStopBank Team</p>" +
                "</div>" +
                "</div>";
    }

    @Override
    public String getRegistrationOtpEmailTemplate(String name, String otp) {
        return "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #ddd; padding: 20px;\">" +
                "<div style=\"text-align: center; border-bottom: 2px solid #3f51b5; padding-bottom: 10px; margin-bottom: 20px;\">" +
                "<h2 style=\"color: #3f51b5; margin: 0;\">OneStopBank</h2>" +
                "<p style=\"margin: 5px 0; color: #555;\">Email Verification</p>" +
                "</div>" +
                "<p>Dear <strong>" + name + "</strong>,</p>" +
                "<p>Thank you for registering with OneStopBank! Please use the following One Time Password (OTP) to verify your email address and complete your registration:</p>" +
                "<h2 style=\"background: #3f51b5; text-align: center; margin: 20px auto; width: max-content; padding: 10px 20px; color: #fff; letter-spacing: 5px; border-radius: 4px;\">" +
                otp + "</h2>" +
                "<p>This OTP is valid for 5 minutes. Do not share this code with anyone.</p>" +
                "<div style=\"margin-top: 30px; font-size: 12px; color: #888; text-align: center;\">" +
                "<p>Regards,<br/>The OneStopBank Team</p>" +
                "</div>" +
                "</div>";
    }

    public void sendEmailWithAttachment(String to, String subject, String text, String attachmentFilePath) {
        try {
            val message = mailSender.createMimeMessage();
            val helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // Set the second parameter to true to send HTML content

            // Add an attachment to the email
            val attachmentFile = new File(attachmentFilePath);
            helper.addAttachment(attachmentFile.getName(), attachmentFile);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}", to, e);
        }
    }


}

package com.webapp.bankingportal.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {

    @Async
    public CompletableFuture<Void> sendEmail(String to, String subject, String text);

    public String getLoginEmailTemplate(String name, String loginTime, String loginLocation);

    public String getOtpLoginEmailTemplate(String name, String accountNumber, String otp);

    public String getBankStatementEmailTemplate(String name, String statementText);

    public String getTransactionAlertEmailTemplate(String name, String accountNumber, String transactionType, String date, String info, double amount);

    public String getWelcomeEmailTemplate(String name, String accountNumber, String branch, String ifsc);

    public String getRegistrationOtpEmailTemplate(String name, String otp);
}

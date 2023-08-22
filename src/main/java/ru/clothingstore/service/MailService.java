package ru.clothingstore.service;

public interface MailService {
    void sendEmail(String emailTo, String subject, String message);
}

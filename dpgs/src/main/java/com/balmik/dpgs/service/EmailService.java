package com.balmik.dpgs.service;

public interface EmailService {

    void sendEmail(String to, String subject, String body);
}

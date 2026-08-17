package com.balmik.dpgs.service.impl;

import com.balmik.dpgs.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmail(String to, String subject, String body) {

        log.info("========== EMAIL SENT ==========");
        log.info("To      : {}", to);
        log.info("Subject : {}", subject);
        log.info("Body    : {}", body);
        log.info("================================");

    }
}

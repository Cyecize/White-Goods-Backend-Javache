package com.cyecize.app.api.mail;

import com.cyecize.summer.common.annotations.Bean;
import com.cyecize.summer.common.annotations.BeanConfig;
import com.cyecize.summer.common.annotations.Configuration;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import lombok.RequiredArgsConstructor;

@BeanConfig
@RequiredArgsConstructor
public class MailSessionConfig {

    @Configuration("mail.smtp.auth")
    private final boolean auth;

    @Configuration("mail.smtp.starttls.enable")
    private final boolean enableTls;

    @Configuration("mail.smtp.host")
    private final String smtpHost;

    @Configuration("mail.smtp.port")
    private final int smtpPort;

    @Configuration("mail.username")
    private final String mailUsername;

    @Configuration("mail.password")
    private final String mailPassword;

    @Bean
    public Session getSession() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", this.auth);
        prop.put("mail.smtp.starttls.enable", this.enableTls);
        prop.put("mail.smtp.host", this.smtpHost);
        prop.put("mail.smtp.port", this.smtpPort);

        return Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUsername, mailPassword);
            }
        });
    }
}

package org.example.code.config;

import org.eclipse.angus.mail.util.MailSSLSocketFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.security.GeneralSecurityException;
import java.util.Properties;
@Configuration
public class MailConfig {
    @Bean
    public JavaMailSender getJavaMailSender() throws GeneralSecurityException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(465);

        mailSender.setUsername("pethaven321@gmail.com");
        mailSender.setPassword("rnst ncth oboj knfg");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "true");

        MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
        socketFactory.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.socketFactory", socketFactory);

        return mailSender;
    }
}

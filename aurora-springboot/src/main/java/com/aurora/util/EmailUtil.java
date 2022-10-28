package com.aurora.util;

import com.aurora.model.dto.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailUtil {

    @Value("${spring.mail.username}")
    private String email;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendHtmlMail(EmailDTO emailDTO) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            Context context = new Context();
            context.setVariables(emailDTO.getCommentMap());
            String process = templateEngine.process(emailDTO.getTemplate(), context);
            mimeMessageHelper.setFrom(email);
            mimeMessageHelper.setTo(emailDTO.getEmail());
            mimeMessageHelper.setSubject(emailDTO.getSubject());
            mimeMessageHelper.setText(process, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}

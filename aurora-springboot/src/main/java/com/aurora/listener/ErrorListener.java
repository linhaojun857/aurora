package com.aurora.listener;

import com.aurora.entity.Error;
import com.aurora.event.ErrorEvent;
import com.aurora.event.SysLogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ErrorListener {

    @Value("${spring.mail.username}")
    private String email;

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    @EventListener(ErrorEvent.class)
    public void notice(ErrorEvent errorEvent){
        Error error = (Error) errorEvent.getSource();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo("lhj13401792631@163.com");
        message.setSubject("时间:"+error.getLocalDateTime());
        message.setText(error.getMessage());
//        javaMailSender.send(message);
    }

}

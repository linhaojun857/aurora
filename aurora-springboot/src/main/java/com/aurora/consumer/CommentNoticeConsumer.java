package com.aurora.consumer;

import com.alibaba.fastjson.JSON;
import com.aurora.model.dto.EmailDTO;
import com.aurora.util.EmailUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import static com.aurora.constant.CommonConstant.*;
import static com.aurora.constant.RabbitMQConstant.EMAIL_QUEUE;

/**
 * @author 花未眠
 * 通知邮箱
 **/
@Component
@RabbitListener(queues = EMAIL_QUEUE)
public class CommentNoticeConsumer {

    @Autowired
    private EmailUtil emailUtil;

    @RabbitHandler
    public void process(byte[] data) {
        EmailDTO mailDTO = JSON.parseObject(new String(data), EmailDTO.class);
        if (CAPTCHA.equals(mailDTO.getSubject()) || CHECK_REMIND.equals(mailDTO.getSubject())) {
            emailUtil.sendSimpleMail(mailDTO);
        }
        if (COMMENT_REMIND.equals(mailDTO.getSubject())) {
            emailUtil.sendHtmlMail(mailDTO);
        }
    }
}

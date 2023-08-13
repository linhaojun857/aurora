package com.aurora.config;

import com.alibaba.fastjson.JSON;
import com.aurora.model.mq.AuroraCorrelationData;
import com.aurora.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author zyl
 * @date 2023/8/12 13:36
 */
@Slf4j
@SuppressWarnings("all")
@Component
public class MQProducerAckConfig implements RabbitTemplate.ReturnCallback, RabbitTemplate.ConfirmCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisService redisService;

    @PostConstruct
    public void init() {
        rabbitTemplate.setReturnCallback(this);
        rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * 判断消息是否到交换机
     *
     * @param correlationData { id Message returnedMessage }
     * @param ack             true: 表示消息到交换机了，false:消息没有到交换机
     * @param relayText       表示信息提示
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String relayText) {
        if (ack) {
            log.info("信息到交换机{}", correlationData);
            System.out.println("消息到交换机{}," + correlationData);
        } else {
            log.error("消息没有到交换机:{}", relayText);
            //   如果消息未到交换机就重试.
            this.retryMsg(correlationData);
        }
    }


    /**
     * 判断消息是否到队列. 当消息没有到队列的时候，才会执行这个方法.
     *
     * @param message    消息对象
     * @param replyCode  应答码
     * @param replyText  原因
     * @param exchange   交换机
     * @param routingKey 路由键
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息路由queue失败，应答码={}，原因={}，交换机={}，路由键={}，消息={}",
                replyCode, replyText, exchange, routingKey, message.toString());

        String uuId = message.getMessageProperties().getHeader("spring_returned_message_correlation");
        String gmallJson = (String) this.redisService.get(uuId);
        AuroraCorrelationData auroraCorrelationData = JSON.parseObject(gmallJson, AuroraCorrelationData.class);
        //  调用重试方法.
        retryMsg(auroraCorrelationData);
    }

    /**
     * 重试方法：
     *
     * @param correlationData
     */
    private void retryMsg(CorrelationData correlationData) {
        //  先获取缓存中的数据--有重试次数.
        AuroraCorrelationData auroraCorrelationData = (AuroraCorrelationData) correlationData;
        int retryCount = auroraCorrelationData.getRetryCount();
        //  判断重试次数，还是否需要继续重复发送消息.
        if (retryCount >= 3) {
            log.error("重试次数已到{}", +retryCount);
            //TODO: 需要将消息持久化到数据库,后续由人工处理. insert into msg values ();
        } else {
            //  修改重试次数写回缓存.
            auroraCorrelationData.setRetryCount(++retryCount);
            this.redisService.set(auroraCorrelationData.getId(), JSON.toJSONString(auroraCorrelationData), 10);
            //  重试
            this.rabbitTemplate.convertAndSend(auroraCorrelationData.getExchange(),
                    auroraCorrelationData.getRoutingKey(),
                    auroraCorrelationData.getMessage(),
                    auroraCorrelationData);
        }
    }
}

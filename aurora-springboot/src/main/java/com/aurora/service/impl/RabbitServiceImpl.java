package com.aurora.service.impl;

import com.alibaba.fastjson.JSON;
import com.aurora.model.mq.AuroraCorrelationData;
import com.aurora.service.RabbitService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author zyl
 * @date 2023/8/12 18:51
 */
@Service
public class RabbitServiceImpl implements RabbitService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送消息方法
     * @param exchange
     * @param routingKey
     * @param obj
     * @return
     */
    public Boolean sendMsg(String exchange,String routingKey,Object obj){

        AuroraCorrelationData auroraCorrelationData = new AuroraCorrelationData();
        String uuId = UUID.randomUUID().toString();
        auroraCorrelationData.setId(uuId);
        auroraCorrelationData.setExchange(exchange);
        auroraCorrelationData.setRoutingKey(routingKey);
        auroraCorrelationData.setMessage(obj);

        this.redisTemplate.opsForValue().set(uuId, JSON.toJSONString(auroraCorrelationData),10, TimeUnit.MINUTES);

        this.rabbitTemplate.convertAndSend(exchange,routingKey,obj, auroraCorrelationData);

        return true;
    }

}

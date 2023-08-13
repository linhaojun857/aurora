package com.aurora.model.mq;

import lombok.Data;
import org.springframework.amqp.rabbit.connection.CorrelationData;

@Data
public class AuroraCorrelationData extends CorrelationData {

    //  消息主体
    private Object message;
    //  交换机
    private String exchange;
    //  路由键
    private String routingKey;
    //  重试次数
    private int retryCount = 0;

}
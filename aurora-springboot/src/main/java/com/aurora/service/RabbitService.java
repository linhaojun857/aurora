package com.aurora.service;

/**
 * @author zyl
 * @date 2023/8/12 18:51
 */
public interface RabbitService {
    Boolean sendMsg(String exchange,String routingKey,Object obj);
}

package com.aurora.listener;

import com.aurora.entity.OperationLog;
import com.aurora.event.OperationLogEvent;
import com.aurora.mapper.OperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OperationLogListener {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Async
    @EventListener(OperationLogEvent.class)
    public void saveOperationLog(OperationLogEvent operationLogEvent) {
        operationLogMapper.insert((OperationLog) operationLogEvent.getSource());
    }
}

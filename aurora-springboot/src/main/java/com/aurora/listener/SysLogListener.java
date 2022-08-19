package com.aurora.listener;

import com.aurora.entity.OperationLog;
import com.aurora.event.SysLogEvent;
import com.aurora.mapper.OperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SysLogListener {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Async
    @EventListener(SysLogEvent.class)
    public void saveOperationLog(SysLogEvent sysLogEvent) {
        operationLogMapper.insert((OperationLog) sysLogEvent.getSource());
    }
}

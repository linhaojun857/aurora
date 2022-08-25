package com.aurora.listener;

import com.aurora.entity.ExceptionLog;
import com.aurora.event.ExceptionLogEvent;
import com.aurora.mapper.ExceptionLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ExceptionLogListener {

    @Autowired
    private ExceptionLogMapper exceptionLogMapper;

    @Async
    @EventListener(ExceptionLogEvent.class)
    public void saveExceptionLog(ExceptionLogEvent exceptionLogEvent) {
        exceptionLogMapper.insert((ExceptionLog) exceptionLogEvent.getSource());
    }
}

package com.aurora.event;

import com.aurora.entity.ExceptionLog;
import org.springframework.context.ApplicationEvent;

/**
 * 异常日志事件
 */
public class ExceptionLogEvent extends ApplicationEvent {
    public ExceptionLogEvent(ExceptionLog exceptionLog) {
        super(exceptionLog);
    }
}

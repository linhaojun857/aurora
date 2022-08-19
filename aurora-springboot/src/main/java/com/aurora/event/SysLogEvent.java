package com.aurora.event;

import com.aurora.entity.OperationLog;
import org.springframework.context.ApplicationEvent;

/**
 * 日志事件
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(OperationLog source) {
        super(source);
    }
}

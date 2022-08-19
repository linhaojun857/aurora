package com.aurora.event;

import com.aurora.entity.Error;
import org.springframework.context.ApplicationEvent;

/**
 * 报错事件
 */
public class ErrorEvent extends ApplicationEvent {

    public ErrorEvent(Error source) {
        super(source);
    }
}

package com.aurora.quartz;

import com.aurora.entity.Job;
import com.aurora.utils.JobInvokeUtils;
import org.quartz.JobExecutionContext;

/**
 * @author 花未眠
 * 定时任务处理（允许并发执行）
 */
public class QuartzJobExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobExecutionContext context, Job job) throws Exception {
        JobInvokeUtils.invokeMethod(job);
    }
}

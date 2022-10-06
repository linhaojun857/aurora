package com.aurora.quartz;

import com.aurora.entity.Job;
import com.aurora.util.JobInvokeUtil;
import org.quartz.JobExecutionContext;

public class QuartzJobExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobExecutionContext context, Job job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}

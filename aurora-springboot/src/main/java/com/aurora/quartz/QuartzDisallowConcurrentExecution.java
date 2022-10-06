package com.aurora.quartz;

import com.aurora.entity.Job;
import com.aurora.util.JobInvokeUtil;
import org.quartz.JobExecutionContext;

public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, Job job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}

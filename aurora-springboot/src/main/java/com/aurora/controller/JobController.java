package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.model.dto.JobDTO;
import com.aurora.service.JobService;
import com.aurora.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.aurora.constant.OptTypeConstant.*;

@Api(tags = "定时任务模块")
@RestController
public class JobController {

    @Autowired
    private JobService jobService;

    @OptLog(optType = SAVE)
    @ApiOperation("添加定时任务")
    @PostMapping("/admin/jobs")
    public Result<?> saveJob(@RequestBody JobVO jobVO) {
        jobService.saveJob(jobVO);
        return Result.ok();
    }

    @OptLog(optType = UPDATE)
    @ApiOperation("修改定时任务")
    @PutMapping("/admin/jobs")
    public Result<?> updateJob(@RequestBody JobVO jobVO) {
        jobService.updateJob(jobVO);
        return Result.ok();
    }

    @OptLog(optType = DELETE)
    @ApiOperation("删除定时任务")
    @DeleteMapping("/admin/jobs")
    public Result<?> deleteJobById(@RequestBody List<Integer> jobIds) {
        jobService.deleteJobs(jobIds);
        return Result.ok();
    }

    @ApiOperation("根据id获取任务")
    @GetMapping("/admin/jobs/{id}")
    public Result<JobDTO> getJobById(@PathVariable("id") Integer jobId) {
        return Result.ok(jobService.getJobById(jobId));
    }

    @ApiOperation("获取任务列表")
    @GetMapping("/admin/jobs")
    public Result<PageResult<JobDTO>> listJobs(JobSearchVO jobSearchVO) {
        return Result.ok(jobService.listJobs(jobSearchVO));
    }

    @ApiOperation("更改任务的状态")
    @PutMapping("/admin/jobs/status")
    public Result<?> updateJobStatus(@RequestBody JobStatusVO jobStatusVO) {
        jobService.updateJobStatus(jobStatusVO);
        return Result.ok();
    }

    @ApiOperation("执行某个任务")
    @PutMapping("/admin/jobs/run")
    public Result<?> runJob(@RequestBody JobRunVO jobRunVO) {
        jobService.runJob(jobRunVO);
        return Result.ok();
    }

    @ApiOperation("获取所有job分组")
    @GetMapping("/admin/jobs/jobGroups")
    public Result<List<String>> listJobGroup() {
        return Result.ok(jobService.listJobGroups());
    }
}

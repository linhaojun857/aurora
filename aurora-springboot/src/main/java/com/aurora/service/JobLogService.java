package com.aurora.service;


import com.aurora.dto.JobLogDTO;
import com.aurora.entity.JobLog;
import com.aurora.vo.JobLogSearchVO;
import com.aurora.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface JobLogService extends IService<JobLog> {

    PageResult<JobLogDTO> listJobLogs(JobLogSearchVO jobLogSearchVO);

    void deleteJobLogs(List<Integer> ids);

    void cleanJobLogs();

    List<String> listJobLogGroups();
}

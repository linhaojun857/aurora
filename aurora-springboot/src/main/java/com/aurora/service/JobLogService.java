package com.aurora.service;


import com.aurora.model.dto.JobLogDTO;
import com.aurora.entity.JobLog;
import com.aurora.model.vo.JobLogSearchVO;
import com.aurora.model.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface JobLogService extends IService<JobLog> {

    PageResult<JobLogDTO> listJobLogs(JobLogSearchVO jobLogSearchVO);

    void deleteJobLogs(List<Integer> ids);

    void cleanJobLogs();

    List<String> listJobLogGroups();
}

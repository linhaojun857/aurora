package com.aurora.service;

import com.aurora.dto.ExceptionLogDTO;
import com.aurora.entity.ExceptionLog;
import com.aurora.vo.ConditionVO;
import com.aurora.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ExceptionLogService extends IService<ExceptionLog> {

    PageResult<ExceptionLogDTO> listExceptionLogs(ConditionVO conditionVO);
}

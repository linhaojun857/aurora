package com.aurora.service;

import com.aurora.model.dto.ExceptionLogDTO;
import com.aurora.entity.ExceptionLog;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ExceptionLogService extends IService<ExceptionLog> {

    PageResult<ExceptionLogDTO> listExceptionLogs(ConditionVO conditionVO);
}

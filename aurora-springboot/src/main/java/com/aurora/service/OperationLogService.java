package com.aurora.service;

import com.aurora.dto.OperationLogDTO;
import com.aurora.entity.OperationLog;
import com.aurora.vo.ConditionVO;
import com.aurora.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OperationLogService extends IService<OperationLog> {

    PageResult<OperationLogDTO> listOperationLogs(ConditionVO conditionVO);
}

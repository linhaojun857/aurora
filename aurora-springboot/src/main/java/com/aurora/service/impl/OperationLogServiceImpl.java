package com.aurora.service.impl;

import com.aurora.dto.OperationLogDTO;
import com.aurora.entity.OperationLog;
import com.aurora.mapper.OperationLogMapper;
import com.aurora.service.OperationLogService;
import com.aurora.utils.BeanCopyUtils;
import com.aurora.utils.PageUtils;
import com.aurora.vo.ConditionVO;
import com.aurora.vo.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public PageResult<OperationLogDTO> listOperationLogs(ConditionVO conditionVO) {
        Page<OperationLog> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        Page<OperationLog> operationLogPage = this.page(page, new LambdaQueryWrapper<OperationLog>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), OperationLog::getOptModule, conditionVO.getKeywords())
                .or()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), OperationLog::getOptDesc, conditionVO.getKeywords())
                .orderByDesc(OperationLog::getId));
        List<OperationLogDTO> operationLogDTOs = BeanCopyUtils.copyList(operationLogPage.getRecords(), OperationLogDTO.class);
        return new PageResult<>(operationLogDTOs, (int) operationLogPage.getTotal());
    }
}

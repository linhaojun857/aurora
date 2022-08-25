package com.aurora.service.impl;

import com.aurora.dto.ExceptionLogDTO;
import com.aurora.entity.ExceptionLog;
import com.aurora.mapper.ExceptionLogMapper;
import com.aurora.service.ExceptionLogService;
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
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {


    @Override
    public PageResult<ExceptionLogDTO> listExceptionLogs(ConditionVO conditionVO) {
        Page<ExceptionLog> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        Page<ExceptionLog> exceptionLogPage = this.page(page, new LambdaQueryWrapper<ExceptionLog>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), ExceptionLog::getOptDesc, conditionVO.getKeywords())
                .orderByDesc(ExceptionLog::getId));
        List<ExceptionLogDTO> exceptionLogDTOs = BeanCopyUtils.copyList(exceptionLogPage.getRecords(), ExceptionLogDTO.class);
        return new PageResult<>(exceptionLogDTOs, (int) exceptionLogPage.getTotal());
    }
}

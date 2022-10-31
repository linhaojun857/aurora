package com.aurora.mapper;

import com.aurora.model.dto.UserAdminDTO;
import com.aurora.entity.UserAuth;
import com.aurora.model.vo.ConditionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    List<UserAdminDTO> listUsers(@Param("current") Long current, @Param("size") Long size, @Param("conditionVO") ConditionVO conditionVO);

    Integer countUser(@Param("conditionVO") ConditionVO conditionVO);

}

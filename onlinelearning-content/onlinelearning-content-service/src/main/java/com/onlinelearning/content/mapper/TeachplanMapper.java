package com.onlinelearning.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlinelearning.content.mapper.TeachplanMapper;
import com.onlinelearning.content.model.dto.TeachPlanDto;
import com.onlinelearning.content.model.po.Teachplan;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {
    //课程计划查询
    public List<TeachPlanDto> selectTreeNodes(Long courseId);

}

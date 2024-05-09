package com.onlinelearning.content.service;

import com.onlinelearning.content.model.dto.SaveTeachPlanDto;
import com.onlinelearning.content.model.dto.TeachPlanDto;

import java.util.List;

public interface TeachPlanService {
    //根据id查询课程计划
    public List<TeachPlanDto> findTeachPlanTree(Long courseId);
    //新增、修改、保存
    public void saveTeachPlan(SaveTeachPlanDto saveTeachPlanDto);
    public void deleteTeachplan(Long teachplanId);
    void orderByTeachplan(String moveType, Long teachplanId);
}

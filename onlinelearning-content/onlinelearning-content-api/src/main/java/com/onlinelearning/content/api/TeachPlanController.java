package com.onlinelearning.content.api;

import com.onlinelearning.content.model.dto.SaveTeachPlanDto;
import com.onlinelearning.content.model.dto.TeachPlanDto;
import com.onlinelearning.content.service.TeachPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "课程计划编辑相关的接口",tags = "课程计划编辑相关的接口")
@RestController
public class TeachPlanController {
    @Autowired
    TeachPlanService teachPlanService;
    @ApiOperation("查询课程计划树形结构")
    //查询课程计划
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachPlanDto> getTreeNodes(@PathVariable Long courseId){
        List<TeachPlanDto> teachPlanTree = teachPlanService.findTeachPlanTree(courseId);
        return teachPlanTree;
    }
    @ApiOperation("课程计划创建或者修改")
    @PostMapping("/teachplan")
    public void saveTeachPlan(@RequestBody SaveTeachPlanDto saveTeachPlanDto){
        teachPlanService.saveTeachPlan(saveTeachPlanDto);
    }
    @ApiOperation("课程计划删除")
    @DeleteMapping("/teachplan/{teachplanId}")
    public void deleteTeachplan(@PathVariable Long teachplanId) {
        teachPlanService.deleteTeachplan(teachplanId);
    }
    @ApiOperation("课程计划排序")
    @PostMapping("/teachplan/{moveType}/{teachplanId}")
    public void orderByTeachplan(@PathVariable String moveType, @PathVariable Long teachplanId) {
        teachPlanService.orderByTeachplan(moveType, teachplanId);
    }
}

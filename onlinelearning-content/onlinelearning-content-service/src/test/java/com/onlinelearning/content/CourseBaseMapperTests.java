package com.onlinelearning.content;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlinelearning.base.model.PageParams;
import com.onlinelearning.base.model.PageResult;
import com.onlinelearning.content.mapper.CourseBaseMapper;
import com.onlinelearning.content.model.dto.QueryCourseParamsDto;
import com.onlinelearning.content.model.po.CourseBase;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseBaseMapperTests {
    @Autowired
    CourseBaseMapper courseBaseMapper;
    @Test
    public void testCourseBaseMapper(){
        CourseBase courseBase = courseBaseMapper.selectById(1);
        Assertions.assertNotNull(courseBase);
        //分页查询测试
        //查询条件
        QueryCourseParamsDto queryCourseParamsDto = new QueryCourseParamsDto();
        queryCourseParamsDto.setCourseName("java");
        //拼装查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //根据名称模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(courseBase.getName()),CourseBase::getName,courseBase.getName());
        //根据审核状态精确查询
        queryWrapper.eq(StringUtils.isNotEmpty(courseBase.getAuditStatus()),CourseBase::getAuditStatus,courseBase.getAuditStatus());
        //根据课程发布状态精确查询
        queryWrapper.eq(StringUtils.isNotEmpty(courseBase.getStatus()),CourseBase::getStatus,courseBase.getStatus());
        //创建page分页参数对象
        PageParams pageParams = new PageParams();
        pageParams.setPageNo(1l);
        pageParams.setPageSize(2l);
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        //开始分页查询
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);
        //获取数据列表
        List<CourseBase> items = pageResult.getRecords();
        //获取总记录数
        long total = pageResult.getTotal();
        //

        PageResult<CourseBase> CourseBasePageResult = new PageResult<>(items,total,pageParams.getPageNo(),pageParams.getPageSize());

    }
}

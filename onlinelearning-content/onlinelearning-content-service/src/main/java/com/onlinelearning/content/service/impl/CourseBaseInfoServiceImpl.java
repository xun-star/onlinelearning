package com.onlinelearning.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlinelearning.base.exception.OnlienLearningException;
import com.onlinelearning.base.model.PageParams;
import com.onlinelearning.base.model.PageResult;
import com.onlinelearning.content.mapper.*;
import com.onlinelearning.content.model.dto.AddCourseDto;
import com.onlinelearning.content.model.dto.CourseBaseInfoDto;
import com.onlinelearning.content.model.dto.EditCourseDto;
import com.onlinelearning.content.model.dto.QueryCourseParamsDto;
import com.onlinelearning.content.model.po.CourseBase;
import com.onlinelearning.content.model.po.CourseMarket;
import com.onlinelearning.content.model.po.CourseTeacher;
import com.onlinelearning.content.model.po.Teachplan;
import com.onlinelearning.content.service.CourseBaseInfoService;
import com.onlinelearning.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
    @Autowired
    CourseBaseMapper courseBaseMapper;
    @Autowired
    CourseMarketMapper courseMarketMapper;
    @Autowired
    CourseCategoryMapper courseCategoryMapper;
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    CourseTeacherMapper courseTeacherMapper;
    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {
        //拼装查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //根据名称模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()),CourseBase::getName,queryCourseParamsDto.getCourseName());
        //根据审核状态精确查询
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()),CourseBase::getAuditStatus,queryCourseParamsDto.getAuditStatus());
        //根据课程发布状态精确查询
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getPublishStatus()),CourseBase::getStatus,queryCourseParamsDto.getPublishStatus());

        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        //开始分页查询
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);
        //获取数据列表
        List<CourseBase> items = pageResult.getRecords();
        //获取总记录数
        long total = pageResult.getTotal();
        //

        PageResult<CourseBase> CourseBasePageResult = new PageResult<>(items,total,pageParams.getPageNo(),pageParams.getPageSize());
        return CourseBasePageResult;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto) {
        //参数的合法性校验



        //向courseBase表添加信息
        CourseBase courseBaseNew = new CourseBase();
        BeanUtils.copyProperties(addCourseDto,courseBaseNew);//属性名称一致，就可以拷贝
        courseBaseNew.setCompanyId(companyId);
        courseBaseNew.setCreateDate(LocalDateTime.now());
        //审核状态默认为未提交
        courseBaseNew.setAuditStatus("202002");
        //发布状态为未发布
        courseBaseNew.setStatus("203001");
        int insert = courseBaseMapper.insert(courseBaseNew);
        if(insert <= 0){
            //throw new RuntimeException("添加课程失败");
            OnlienLearningException.cast("添加课程失败");
        }
        //向course——market表添加信息
        CourseMarket courseMarketNew = new CourseMarket();
        Long courseId = courseBaseNew.getId();
        courseMarketNew.setId(courseId);
        BeanUtils.copyProperties(addCourseDto,courseMarketNew);
        saveCourseMarket(courseMarketNew);
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(courseId);
        return courseBaseInfo;
    }
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId){
        //从课程基本表中查询
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null){
            return null;
        }
        //从营销表查询
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        //组装到一起
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        if(courseMarket != null){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }
        //todo:课程分类的名称设置到courseBaseInfoDto中
        return courseBaseInfoDto;
    }

    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto) {
        //拿到课程id
        Long courseId = editCourseDto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null){
            OnlienLearningException.cast("课程不存在");
        }
        //数据的合法性校验
        if(!companyId.equals(courseBase.getCompanyId())){
            OnlienLearningException.cast("本机构只能修改本机构的课程");
        }
        //封装数据
        BeanUtils.copyProperties(editCourseDto,courseBase);
        //修改时间
        courseBase.setChangeDate(LocalDateTime.now());
        //更新至数据库
        int i = courseBaseMapper.updateById(courseBase);
        if(i <= 0){
            OnlienLearningException.cast("更新失败");
        }
        //更新营销信息
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(editCourseDto,courseMarket);
        saveCourseMarket(courseMarket);
        //查询课程信息
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(courseId);
        BeanUtils.copyProperties(courseMarket,courseBaseInfo);
        return courseBaseInfo;
    }

    @Override
    public void delectCourse(Long companyId, Long courseId) {
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (!companyId.equals(courseBase.getCompanyId()))
            OnlienLearningException.cast("只允许删除本机构的课程");
        // 删除课程教师信息
        LambdaQueryWrapper<CourseTeacher> teacherLambdaQueryWrapper = new LambdaQueryWrapper<>();
        teacherLambdaQueryWrapper.eq(CourseTeacher::getCourseId, courseId);
        courseTeacherMapper.delete(teacherLambdaQueryWrapper);
        // 删除课程计划
        LambdaQueryWrapper<Teachplan> teachplanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        teachplanLambdaQueryWrapper.eq(Teachplan::getCourseId, courseId);
        teachplanMapper.delete(teachplanLambdaQueryWrapper);
        // 删除营销信息
        courseMarketMapper.deleteById(courseId);
        // 删除课程基本信息
        courseBaseMapper.deleteById(courseId);
    }

    //从数据库查询数据，存在则更新，不存在则添加
    private int saveCourseMarket(CourseMarket courseMarket){
        //参数的合法性校验
        String charge = courseMarket.getCharge();
        if(StringUtils.isEmpty(charge)){
            throw new RuntimeException("收费规则为空");
        }
        if(charge.equals("201001")){
            if(courseMarket.getPrice()==null || courseMarket.getPrice() <= 0){
                //throw new RuntimeException("课程价格有误");
                OnlienLearningException.cast("课程价格不能为空，且必须大于0");
            }
        }
        //从数据库查询数据，存在则更新，不存在则添加
        Long id = courseMarket.getId();
        CourseMarket courseMarket1 = courseMarketMapper.selectById(id);
        if(courseMarket1 == null){
            //插入数据库
            int insert = courseMarketMapper.insert(courseMarket);
            return insert;
        }else{
            //更新数据库
            BeanUtils.copyProperties(courseMarket,courseMarket1);
            courseMarket1.setId(courseMarket.getId());
            int i = courseMarketMapper.updateById(courseMarket1);
            return i;
        }
    }
}

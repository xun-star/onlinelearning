package com.onlinelearning.content.service;

import com.onlinelearning.base.model.PageParams;
import com.onlinelearning.base.model.PageResult;
import com.onlinelearning.content.model.dto.AddCourseDto;
import com.onlinelearning.content.model.dto.CourseBaseInfoDto;
import com.onlinelearning.content.model.dto.QueryCourseParamsDto;
import com.onlinelearning.content.model.po.CourseBase;

import java.util.List;

public interface CourseBaseInfoService {
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);
    public CourseBaseInfoDto createCourseBase(Long companyId,AddCourseDto addCourseDto);
}

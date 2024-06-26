package com.onlinelearning.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlinelearning.base.model.PageParams;
import com.onlinelearning.base.model.PageResult;
import com.onlinelearning.media.mapper.MediaFilesMapper;
import com.onlinelearning.media.model.dto.QueryMediaParamsDto;
import com.onlinelearning.media.model.po.MediaFiles;
import com.onlinelearning.media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description TODO
 * @author Mr.M
 * @date 2022/9/10 8:58
 * @version 1.0
 */
 @Service
public class MediaFileServiceImpl implements MediaFileService {

  @Autowired
 MediaFilesMapper mediaFilesMapper;

 @Override
 public PageResult<MediaFiles> queryMediaFiels(Long companyId,PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto) {

  //构建查询条件对象
  LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
  
  //分页对象
  Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
  // 查询数据内容获得结果
  Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);
  // 获取数据列表
  List<MediaFiles> list = pageResult.getRecords();
  // 获取数据总数
  long total = pageResult.getTotal();
  // 构建结果集
  PageResult<MediaFiles> mediaListResult = new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());
  return mediaListResult;

 }
}

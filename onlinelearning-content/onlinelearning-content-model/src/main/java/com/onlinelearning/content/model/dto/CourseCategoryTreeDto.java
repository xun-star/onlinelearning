package com.onlinelearning.content.model.dto;

import com.onlinelearning.content.model.po.CourseCategory;
import lombok.Data;

import java.util.List;

@Data
public class CourseCategoryTreeDto extends CourseCategory implements java.io.Serializable{
    List<CourseCategoryTreeDto> childrenTreeNodes;
}

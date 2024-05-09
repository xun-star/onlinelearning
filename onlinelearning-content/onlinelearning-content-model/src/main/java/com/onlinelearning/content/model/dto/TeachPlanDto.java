package com.onlinelearning.content.model.dto;

import com.onlinelearning.content.model.po.Teachplan;
import com.onlinelearning.content.model.po.TeachplanMedia;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class TeachPlanDto extends Teachplan {
    //与媒资关联的信息
    private TeachplanMedia teachplanMedia;
    //小章节list
    private List<TeachPlanDto> teachPlanTreeNodes;
}

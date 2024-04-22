package com.onlinelearning.base.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PageParams {
    //当前页码
    @ApiModelProperty(value = "页码")
    private Long pageNo = 1l;
    //每页显示记录数
    @ApiModelProperty(value = "每页记录数")
    private Long pageSize = 10l;

    public PageParams() {
    }

    public PageParams(Long pageNo, Long pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }


}

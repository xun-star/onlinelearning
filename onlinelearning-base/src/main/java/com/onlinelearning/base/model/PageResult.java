package com.onlinelearning.base.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
@Data
@ToString
public class PageResult<T> implements Serializable {
    //数据列表
    private List<T> items;
    //查询总数
    private Long counts;
    //当前页码数
    private Long page;
    //每页展示数量
    private Long pageSize;

    public PageResult(List<T> items, Long counts, Long page, Long pageSize) {
        this.items = items;
        this.counts = counts;
        this.page = page;
        this.pageSize = pageSize;
    }
}

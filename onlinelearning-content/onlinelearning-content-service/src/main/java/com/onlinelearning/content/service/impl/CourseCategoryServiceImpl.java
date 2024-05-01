package com.onlinelearning.content.service.impl;

import com.onlinelearning.content.mapper.CourseCategoryMapper;
import com.onlinelearning.content.model.dto.CourseCategoryTreeDto;
import com.onlinelearning.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@Slf4j
public class CourseCategoryServiceImpl implements CourseCategoryService {
    @Autowired
    CourseCategoryMapper courseCategoryMapper;
    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);
        List<CourseCategoryTreeDto> courseCategoryList = new ArrayList<>();
        //将list转成map，key就是节点的id，value就是courseCategoryTreeDto对象
        Map<String, CourseCategoryTreeDto> mapTemp = courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId())).collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));
        courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId())).forEach(item->{
            //向list写入数据
            if(item.getParentid().equals(id)){
                courseCategoryList.add(item);
            }
            CourseCategoryTreeDto courseCategoryparent = mapTemp.get(item.getParentid());
            if(courseCategoryparent!=null){
                if(courseCategoryparent.getChildrenTreeNodes()==null){
                    courseCategoryparent.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                }
                courseCategoryparent.getChildrenTreeNodes().add(item);
            }
        });
        return courseCategoryList;

    }
}

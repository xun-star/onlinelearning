package com.onlinelearning.content;

import com.onlinelearning.content.model.dto.CourseCategoryTreeDto;
import com.onlinelearning.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
public class CoureseCategoryServiceTests {
    @Autowired
    CourseCategoryService courseCategoryService;
    @Test
    public void testqueryTreeNodes(){
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryService.queryTreeNodes("1");
        System.out.println(courseCategoryTreeDtos);
    }
}

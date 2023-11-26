package com.ssafy.attraction;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssafy.attraction.dto.Attraction;
import com.ssafy.attraction.dto.AttractionKeywordSearchDto;
import com.ssafy.attraction.mapper.AttractionMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
public class AttractionMapperTest {
    @Autowired
    AttractionMapper attractionMapper;

    @Test
    public void selectTest() {
        Attraction attraction = attractionMapper.select(125406);

        assertNotNull(attraction);
        assertEquals("비슬산자연휴양림", attraction.getTitle());
        assertEquals("http://tong.visitkorea.or.kr/cms/resource/62/219162_image2_1.jpg", attraction.getFirstImage());
    }

    @Test
    public void listTest() {
        AttractionKeywordSearchDto option = new AttractionKeywordSearchDto();
        PageHelper.startPage(1, 100000);
        Page<Attraction> list = attractionMapper.list(option);
        assertEquals(35844, list.size());

        option.setKeyword("해운대");
        list = attractionMapper.list(option);

        assertEquals(36, list.size());

        PageHelper.startPage(1, 10);
        option = new AttractionKeywordSearchDto();
        option.setKeyword("해운대");
        list = attractionMapper.list(option);
        assertEquals(10, list.size());

        PageInfo<Attraction> info = list.toPageInfo();
        log.debug("page info : {}", info);
        log.debug("page info -> list : {}", info.getList());

        PageHelper.startPage(1, 10000);
        option = AttractionKeywordSearchDto.builder().keyword("수목원").build();
        list = attractionMapper.list(option);
        assertEquals(80, list.size());
    }
}

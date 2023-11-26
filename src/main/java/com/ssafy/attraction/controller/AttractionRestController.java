package com.ssafy.attraction.controller;

import com.github.pagehelper.Page;
import com.ssafy.attraction.dto.Attraction;
import com.ssafy.attraction.dto.AttractionKeywordSearchDto;
import com.ssafy.attraction.service.AttractionService;
import com.ssafy.paging.PagingResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/attraction")
@Api(tags = {"관광지"})
public class AttractionRestController {

    AttractionService attractionService;

    @Autowired
    public AttractionRestController(AttractionService attractionService){
        this.attractionService = attractionService;
    }

    @GetMapping("/list/paging")
    public ResponseEntity<PagingResult<Attraction>> list(@ModelAttribute AttractionKeywordSearchDto attractionKeywordSearchDto) {
        log.debug("param : {}", attractionKeywordSearchDto);
        Page<Attraction> page = attractionService.list(attractionKeywordSearchDto);

        return new ResponseEntity<>(new PagingResult<>(page), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Attraction select(@PathVariable("id") int contentId){
        return attractionService.select(contentId);
    }
}

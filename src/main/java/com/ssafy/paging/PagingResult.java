package com.ssafy.paging;

import com.github.pagehelper.Page;
import lombok.Data;

import java.util.List;

@Data
public class PagingResult<E>{
    private List<E> data;
    private int pageNum;
    private int numOfRows;
    private int pages;

    public PagingResult(Page<E> page){
        data = page.getResult();
        pageNum = page.getPageNum();
        numOfRows = page.getPageSize();
        pages = page.getPages();
    }
}

package com.wqs.root.util;

import com.github.pagehelper.Page;

import java.util.List;

public class MyPage<T> {
    private int pageNumber;
    private int pageSize;
    private long totalCount;
    private int totalPage;
    private List<T> list;
    public MyPage(){

    }
    public MyPage(Page<T> page){
        this.pageNumber=page.getPageNum();
        this.pageSize=page.getPageSize();
        this.totalCount=page.getTotal();
        this.totalPage=page.getPages();
        this.list=page.getResult();
    }
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}

package com.dity.ssm.util;

import java.util.List;

/**
 * 分页类
 * @author:yuhang
 * @Date:2019/1/18
 */
public class Page<T> {
    /**
     * 当前第几页
     */
    private int pageNum = 1;
    /**
     * 页面大小
     */
    private int pageSize = 10;
    /**
     * 起始行
     */
    private int startRow = 0;
    /**
     * 末行
     */
    private int endRow;
    /**
     * 总数
     */
    private long total = 0;
    /**
     * 总页数
     */
    private int pages = 0;

    /**
     * 分页数据
     */
    private List<T> result;

    public Page ( int pageNum,int pageSize){
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        calculateStartAndEndRow();
    }

    public Page() {
    }


    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        this.pages = (int)(total/pageSize + total%pageSize==0?0:1);
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    /**
     * 计算起始行和结束行
     */
    private void calculateStartAndEndRow() {
        this.startRow = this.pageNum > 0 ? (this.pageNum - 1) * this.pageSize : 0;
        this.endRow = this.startRow + this.pageSize * (this.pageNum > 0 ? 1 : 0);
    }

}

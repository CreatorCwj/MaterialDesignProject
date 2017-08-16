package com.refreshloadview;

/**
 * Created by cwj on 16/7/27.
 * 分页类
 */
public class Page {

    public static final int DEFAULT_FIRST_PAGE_NO = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;

    private int firstPageNo;
    private int pageNo;
    private int pageSize;

    public Page() {
        this(DEFAULT_FIRST_PAGE_NO);
    }

    public Page(int firstPageNo) {
        this(firstPageNo, DEFAULT_PAGE_SIZE);
    }

    public Page(int firstPageNo, int pageSize) {
        this.pageNo = this.firstPageNo = firstPageNo;
        this.pageSize = pageSize;
    }

    /**
     * 前一页
     */
    public void prePage() {
        --pageNo;
        if (pageNo < firstPageNo) {
            pageNo = firstPageNo;
        }
    }

    /**
     * 重置为首页
     */
    public void resetPage() {
        pageNo = firstPageNo;
    }

    /**
     * 下一页
     */
    public void nextPage() {
        ++pageNo;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getFirstPageNo() {
        return firstPageNo;
    }
}

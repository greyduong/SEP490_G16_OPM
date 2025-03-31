package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Pagination data class
 *
 * @param <T> item data type
 */
public class Page<T> {

    private int pageNumber = 1;
    private int pageSize = 10;
    private int totalPage;
    private List<T> data = new ArrayList<>();

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int page) {
        this.pageNumber = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

}

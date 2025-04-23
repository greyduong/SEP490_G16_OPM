package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Pagination data class
 *
 * @param <T> item data type
 */
public class Page<T> {

    private int pageNumber = 1;
    private int pageSize = 10;
    private int totalPage;
    private int totalElements;

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }
    private List<T> data = new ArrayList<>();
    private int totalData;

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

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return """
               Page {
               \tPageNumber = %s
               \tPageSize = %s
               \tTotalPage = %s
               \tData = [
               %s
               \t]
               }
               """.formatted(
                pageNumber,
                pageSize,
                totalPage,
                data.stream().map(item -> {
                    return List.of(item.toString().split("\n"))
                            .stream()
                            .map(line -> "\t\t" + line)
                            .collect(Collectors.joining("\n"));
                }).collect(Collectors.joining(",\n"))
        );
    }
}

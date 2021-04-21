package io.beka.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseListDto<T> {
    private List<T> dataList;
    private int totalPages = 0;
    private int totalElements = 0;
    private boolean isLast;

    public ResponseListDto(List<T> dataList, int totalPages, int totalElements, boolean isLast) {
        this.dataList = dataList;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.isLast = isLast;
    }
}

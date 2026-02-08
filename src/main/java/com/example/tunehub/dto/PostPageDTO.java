package com.example.tunehub.dto;


import java.util.List;


public class PostPageDTO {

    private List<PostResponseDTO> content;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private boolean last;

    public List<PostResponseDTO> getContent() {
        return content;
    }

    public void setContent(List<PostResponseDTO> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}

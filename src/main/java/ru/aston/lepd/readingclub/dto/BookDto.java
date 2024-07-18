package ru.aston.lepd.readingclub.dto;

import java.util.ArrayList;
import java.util.List;

public class BookDto implements Cloneable {

    private String title;
    private Long inventoryNumber;
    private List<Long> authorIds = new ArrayList<>();
    private Long readerId;


    public BookDto() {
    }

    public BookDto(String title, Long inventoryNumber, List<Long> authorIds, Long readerId) {
        this.title = title;
        this.inventoryNumber = inventoryNumber;
        this.authorIds = authorIds;
        this.readerId = readerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(Long inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public List<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<Long> authorIds) {
        this.authorIds = authorIds;
    }

    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }



    @Override
    public BookDto clone() {
        try {
            return (BookDto) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

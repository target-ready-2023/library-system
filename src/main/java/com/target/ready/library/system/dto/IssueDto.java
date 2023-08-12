package com.target.ready.library.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IssueDto {
    private int book_id;
    private int student_id;

    @JsonProperty("book_id")
    public int getBookId() {
        return book_id;
    }

    @JsonProperty("book_id")
    public void setBookId(int book_id) {
        this.book_id = book_id;
    }

    @JsonProperty("student_id")
    public int getStudentId() {
        return student_id;
    }

    @JsonProperty("student_id")
    public void setStudentId(int student_id) {
        this.student_id = student_id;
    }
}

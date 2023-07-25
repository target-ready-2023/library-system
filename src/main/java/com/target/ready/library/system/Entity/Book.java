package com.target.ready.library.system.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @JsonProperty
    private int bookId;
    @JsonProperty
    private String bookName;
    @JsonProperty
    private String bookDescription;

    @JsonProperty
    private int publicationYear;

    @JsonProperty
    private String authorName;
}

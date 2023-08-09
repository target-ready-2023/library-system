package com.target.ready.library.system.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @JsonProperty("book_id")
    @JsonAlias("bookId")
    private int bookId;

    @JsonProperty("book_name")
    @JsonAlias("bookName")
    @NotBlank(message = "Book name should not be empty")
    private String bookName;

    @JsonProperty("book_description")
    @JsonAlias("bookDescription")
    private String bookDescription;

    @JsonProperty("author_name")
    @JsonAlias("authorName")
    @NotBlank(message = "Author name should not be empty")
    private String authorName;

    @JsonProperty("publication_year")
    @JsonAlias("publicationYear")
    @Min(value = 1000, message = "Publication year must be sensible")
    @Max(value = 2023, message = "Publication year must be sensible")
    @Digits(integer = 4, fraction = 0, message = "Publication year must be a 4-digit number")
    private int publicationYear;


}

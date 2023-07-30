package com.target.ready.library.system.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @JsonProperty("inv_book_id")
    @JsonAlias("invBookId")
    private int invBookId;

    @JsonProperty("no_of_copies")
    @JsonAlias("noOfCopies")
    private int noOfCopies;

    @JsonProperty("no_of_books_left")
    @JsonAlias("noOfBooksLeft")
    private int noOfBooksLeft;
}

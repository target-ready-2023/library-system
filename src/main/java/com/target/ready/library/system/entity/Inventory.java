package com.target.ready.library.system.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @JsonProperty("inv_book_id")
    @JsonAlias("invBookId")
    private int invBookId;

    @JsonProperty("no_of_copies")
    @JsonAlias("noOfCopies")
    @Valid
    @Min(value = 1,message = "No of copies of the given book cannot be less than 1")
    private int noOfCopies;

    @JsonProperty("no_of_books_left")
    @JsonAlias("noOfBooksLeft")
    private int noOfBooksLeft;
}

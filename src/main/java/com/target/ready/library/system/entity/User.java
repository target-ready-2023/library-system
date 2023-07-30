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

public class User {

    @JsonProperty("user_id")
    @JsonAlias("userId")
    private int userId;

    @JsonProperty("book_id")
    @JsonAlias("bookId")
    private int bookId;

    @JsonProperty("quantity")
    @JsonAlias("quantity")
    private int quantity;
}

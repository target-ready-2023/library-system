package com.target.ready.library.system.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Category {
    @JsonProperty("category_id")
    @JsonAlias("categoryId")
    private int categoryId;

    @JsonProperty("category_name")
    @JsonAlias("categoryName")
    private String categoryName;
}

package com.target.ready.library.system.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Category {
    private int categoryId;
    @JsonProperty
    private String categoryName;
}

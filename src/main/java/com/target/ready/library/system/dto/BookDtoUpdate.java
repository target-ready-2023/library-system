package com.target.ready.library.system.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.target.ready.library.system.entity.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDtoUpdate {
    @Valid
    private Book book;
    @JsonProperty("category_names")
    @JsonAlias("categoryNames")
    @Valid
    @NotEmpty(message = "Atleast one category should be there")
    private List<@NotBlank(message = "Category name cannot be an empty string")String> categoryNames;
}
package com.target.ready.library.system.Dto;

import com.target.ready.library.system.Entity.Book;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Hidden
public class BookDto {
    private Book book;
    private List<String> categoryNames;
}

package com.target.ready.library.system.Dto;

import com.target.ready.library.system.Entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Book book;
    private List<String> categoryNames;
}

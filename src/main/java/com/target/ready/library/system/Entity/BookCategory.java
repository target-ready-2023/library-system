package com.target.ready.library.system.Entity;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Hidden
public class BookCategory {
    private int id;
    private int bookId;
    private String categoryName;

}

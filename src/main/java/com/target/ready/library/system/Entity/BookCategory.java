package com.target.ready.library.system.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCategory {
    private int id;
    private int bookId;
    private String categoryName;

}

package com.target.ready.library.system.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Book {
    private int bookId;
    private String bookName;
    private String bookDescription;
    private int publicationYear;
}

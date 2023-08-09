package com.target.ready.library.system.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.BookCategory;
import reactor.core.publisher.Mono;

import java.util.List;


public interface BookCategoryRepository {

    public BookCategory addBookCategory(BookCategory bookCategory) throws JsonProcessingException;
    public String deleteBookCategory(int bookId);
    public List<BookCategory> findAllCategoriesByBookId(int bookId);
}

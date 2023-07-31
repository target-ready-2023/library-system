package com.target.ready.library.system.repository;

import com.target.ready.library.system.entity.BookCategory;
import reactor.core.publisher.Mono;

import java.util.List;


public interface BookCategoryRepository {

    public void addBookCategory(BookCategory bookCategory);
    public Mono<Void> deleteBookCategory(int bookId);
    public List<BookCategory> findAllCategoryByBookId(int bookId);
}

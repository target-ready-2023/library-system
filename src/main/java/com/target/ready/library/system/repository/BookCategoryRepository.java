package com.target.ready.library.system.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.exceptions.DataAccessException;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;

import java.util.List;


public interface BookCategoryRepository {

    public BookCategory addBookCategory(BookCategory bookCategory) throws JsonProcessingException, ResourceAlreadyExistsException;
    public String deleteBookCategory(int bookId) throws DataAccessException, ResourceNotFoundException;
    public List<BookCategory> findAllCategoriesByBookId(int bookId);
}

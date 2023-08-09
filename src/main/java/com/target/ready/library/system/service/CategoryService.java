package com.target.ready.library.system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.repository.BookCategoryRepository;
import com.target.ready.library.system.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BookCategoryRepository bookCategoryRepository;


    @Autowired
    public CategoryService(CategoryRepository categoryRepository, BookCategoryRepository bookCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.bookCategoryRepository = bookCategoryRepository;
    }

    public Category addCategory(Category category) throws ResourceAlreadyExistsException,JsonProcessingException {
        return categoryRepository.addCategory(category);

    }

    public Category findCategoryBycategoryName(String categoryName) throws ResourceNotFoundException {
        Category result = categoryRepository.findCategoryBycategoryName(categoryName);
        return result;
    }

    public List<Category> findAllCategories(int page_number, int page_size) {
        try {
            List<Category> response = categoryRepository.findAllCategories(page_number,page_size);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public  BookCategory addBookCategory(BookCategory bookCategory) throws JsonProcessingException,ResourceAlreadyExistsException {
       return bookCategoryRepository.addBookCategory(bookCategory);

    }

    public String deleteBookCategory(int bookId) {
        try {
            return bookCategoryRepository.deleteBookCategory(bookId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete book category", e);
        }
    }

    public List<BookCategory> findAllCategoriesByBookId(int bookId) throws ResourceNotFoundException{

            return bookCategoryRepository.findAllCategoriesByBookId(bookId);

    }

}
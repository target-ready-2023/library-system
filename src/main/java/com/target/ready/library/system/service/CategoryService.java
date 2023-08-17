package com.target.ready.library.system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.exceptions.DataAccessException;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.repository.BookCategoryRepository;
import com.target.ready.library.system.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
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
        Category result = categoryRepository.findCategoryBycategoryName(categoryName.toLowerCase());
        return result;
    }

    public List<Category> findAllCategories(int page_number, int page_size) throws ResourceNotFoundException{

            List<Category> response = categoryRepository.findAllCategories(page_number,page_size);

            return response;

    }

    public Mono<Long> getTotalCategoryCount() {
        return categoryRepository.totalCategories();
    }

    public  BookCategory addBookCategory(BookCategory bookCategory) throws JsonProcessingException,ResourceAlreadyExistsException {
       return bookCategoryRepository.addBookCategory(bookCategory);

    }

    public String deleteBookCategory(int bookId) throws ResourceNotFoundException{

            return bookCategoryRepository.deleteBookCategory(bookId);

    }

    public List<BookCategory> findAllCategoriesByBookId(int bookId) throws ResourceNotFoundException{

            return bookCategoryRepository.findAllCategoriesByBookId(bookId);

    }

}
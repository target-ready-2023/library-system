package com.target.ready.library.system.service;

import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.entity.Category;
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

    public String addCategory(Category category) {
        categoryRepository.addCategory(category);
        return "Category added Successfully!";
    }

    public Category findCategoryBycategoryName(String categoryName) {
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


    public  String addBookCategory(BookCategory bookCategory) {
        bookCategoryRepository.addBookCategory(bookCategory);
        return "Category added to the book successfully";
    }

    public Mono<Void> deleteBookCategory(int bookId) {
        try {
            return bookCategoryRepository.deleteBookCategory(bookId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete", e);
        }
    }

    public List<BookCategory> findAllCategoriesByBookId(int bookId){
        try{
            return bookCategoryRepository.findAllCategoriesByBookId(bookId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
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
//    @Value("${library.baseUrl2}")
//    private String libraryBaseUrl2;

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

    public List<Category> findAllCategories() {
        try {
            List<Category> response = categoryRepository.findAllCategories();

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
//            BookCategory bookCategory = webclient.get().uri(libraryBaseUrl2 + "category/book/" + bookId)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .retrieve()
//                    .bodyToMono(BookCategory.class)
//                    .block();
//            if(bookCategory!=null) {
            return bookCategoryRepository.deleteBookCategory(bookId);
//            return "Book Category deleted successfully!";
//            }
            //return "Book Category is empty";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete", e);
        }
    }

}
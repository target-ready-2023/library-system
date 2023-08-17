package com.target.ready.library.system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.entity.UserProfile;
import com.target.ready.library.system.repository.BookCategoryImplementation;
import com.target.ready.library.system.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {CategoryControllerTest.class})
public class CategoryControllerTest {

    @Mock
    CategoryService categoryService;

    @InjectMocks
    CategoryController categoryController;

    @Mock
    BookCategoryImplementation BookCategoryRepository;

    List<Category> myCategories;

    @Test
    public void findAllCategoriesByBookIdTest(){
        List<BookCategory> bookCategories= new ArrayList<BookCategory>();
        int bookId=5;
        bookCategories.add(new BookCategory(1,bookId,"Horror"));
        bookCategories.add(new BookCategory(2,bookId,"Adventure"));

        when(categoryService.findAllCategoriesByBookId(bookId)).thenReturn(bookCategories);

        ResponseEntity<?> response = categoryController.findAllCategoriesByBookId(bookId);

        assertEquals(HttpStatus.OK,response.getStatusCode());
//        assertEquals(2,response.getBody().size());
    }


    @Test
    public void findAllCategoriesTest() {
        myCategories = new ArrayList<>();
        myCategories.add(new Category(1, "Horror"));
        myCategories.add(new Category(2, "Thriller"));

        // Mocking behavior of the categoryService for valid page number (0)
        when(categoryService.findAllCategories(0, 10)).thenReturn(myCategories);

        // Calling the controller method with a valid page number (0)
        ResponseEntity<?> mockCategories = categoryController.findAllCategories(0);

        // Assertions
        assertNotNull(mockCategories);
        assertEquals(HttpStatus.OK, mockCategories.getStatusCode());

        List<Category> categoriesSize = (List<Category>) mockCategories.getBody();
        assertEquals(2, categoriesSize.size());
    }

    @Test
    public void getTotalCategoriesCountTest() {
        myCategories = new ArrayList<>();
        myCategories.add(new Category(1, "Horror"));
        myCategories.add(new Category(2, "Thriller"));
        Mono<Long> serviceResult=Mono.just(0L);
        when(categoryService.getTotalCategoryCount()).thenReturn(serviceResult);
        ResponseEntity<Mono<Long>> categoryResult=categoryController.getTotalCategoryCount();
        Assertions.assertEquals(HttpStatus.OK, categoryResult.getStatusCode());
        Assertions.assertEquals(serviceResult,categoryResult.getBody());
    }
    @Test
    public void FindAllCategoriesWithNegativePageNumberTest() {
        int pageNumber = -1;

        ResponseEntity<?> response = categoryController.findAllCategories(pageNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());

        verify(categoryService, times(0)).findAllCategories(anyInt(), anyInt());
    }

    @Test
    public void addCategoryTest() throws JsonProcessingException {
        Category category=new Category();
        category.setCategoryName("Horror");
        when(categoryService.addCategory(category)).thenReturn(category);
        ResponseEntity<?> category1=categoryController.addCategory(category);
        assertEquals(HttpStatus.CREATED,category1.getStatusCode());
    }

    @Test
    public void addCategoryWithJsonProcessingExceptionTest() throws JsonProcessingException {
        Category category = new Category();
        category.setCategoryName("Horror");

        when(categoryService.addCategory(category)).thenThrow(JsonProcessingException.class);

        ResponseEntity<?> responseEntity = categoryController.addCategory(category);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("An error occurred while processing the request", responseEntity.getBody());


    }


    @Test
    public void findCategoryByCategoryNameTest(){
        Category category=new Category();
        category.setCategoryName("Horror");
        when(categoryService.findCategoryBycategoryName(category.getCategoryName())).thenReturn(category);
        assertNotNull(categoryController.findCategoryByCategoryName(category.getCategoryName()));
        ResponseEntity<?> category1=categoryController.findCategoryByCategoryName(category.getCategoryName());
        assertEquals(HttpStatus.OK,category1.getStatusCode());
    }


}

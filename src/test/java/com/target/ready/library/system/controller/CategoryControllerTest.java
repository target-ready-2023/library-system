package com.target.ready.library.system.controller;

import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.service.CategoryService;
import com.target.ready.library.system.repository.BookCategoryImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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

        ResponseEntity<List<BookCategory>> response = categoryController.findAllCategoriesByBookId(bookId);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(2,response.getBody().size());
    }

//    @Test
//    public void findAllCategoriesTest(){
//        myCategories = new ArrayList<Category>();
//        myCategories.add(new Category(1,"Horror"));
//        myCategories.add(new Category(2,"Adventure"));
//
//        when(categoryService.findAllCategories(0,5)).thenReturn(myCategories);
//        ResponseEntity<List<Category>> response = categoryController.findAllCategories(0);
//        System.out.println(response);
//
//        assertEquals(HttpStatus.OK,response.getStatusCode());
//        assertEquals(2,response.getBody().size());
//    }



//    @Test
//    public void addCategoryTest(){
//        Category category=new Category();
//        category.setCategoryName("Horror");
//        when(categoryService.addCategory(category)).thenReturn(category);
//        Category category1=categoryController.addCategory(category);
//        assertEquals("Horror",category1.getCategoryName());
//    }
//
//    @Test
//    public void findByCategoryNameTest(){
//    Category category=new Category();
//    category.setCategoryName("Horror");
//    when(categoryService.findByCategoryName(category.getCategoryName())).thenReturn(category);
//    assertNotNull(categoryController.findByCategoryName(category.getCategoryName()));
//    assertEquals("Horror",categoryController.findByCategoryName(category.getCategoryName()).getCategoryName());
//    }
//
//
//    @Test
//    public void addBookCategoryTest(){
//        BookCategory bookCategory=new BookCategory();
//        bookCategory.setBookId(1);
//        bookCategory.setCategoryName("Horror");
//        when(categoryService.addBookCategory(bookCategory)).thenReturn(bookCategory);
//        assertNotNull(categoryController.addBookCategory(bookCategory));
//        assertEquals("Horror",categoryController.addBookCategory(bookCategory).getCategoryName());
//        assertEquals(1,categoryController.addBookCategory(bookCategory).getBookId());
//    }

}

package com.target.ready.library.system.service;
import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.repository.BookCategoryRepository;
import com.target.ready.library.system.repository.CategoryRepository;
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

@SpringBootTest(classes = {CategoryServiceTest.class})
public class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    BookCategoryRepository bookCategoryRepository;

    @InjectMocks
    CategoryService categoryService;

    @Test
    public void findAllCategoriesByBookIdTest(){
        List<BookCategory> bookCategories= new ArrayList<BookCategory>();
        int bookId=5;
        bookCategories.add(new BookCategory(1,bookId,"Horror"));
        bookCategories.add(new BookCategory(2,bookId,"Adventure"));

        when(bookCategoryRepository.findAllCategoriesByBookId(bookId)).thenReturn(bookCategories);
        List<BookCategory> response = categoryService.findAllCategoriesByBookId(bookId);

        assertEquals(2,response.size());
    }

//    @Test
//    public void findAllCategoriesTest(){
//        List<Category> myCategories = new ArrayList<Category>();
//
//        myCategories.add(new Category(1,"Horror"));
//        myCategories.add(new Category(2, "Adventure"));
//
//        when(categoryRepository.findAllCategories()).thenReturn(myCategories);
//        assertEquals(2,categoryService.findAllCategories().size());
//    }

//    @Test
//    public void addCategoryTest(){
//        Category category=new Category();
//        category.setCategoryName("Horror");
//        when(categoryRepository.save(category)).thenAnswer(invocation -> {
//            Category category1 = invocation.getArgument(0);
//            category1.setCategoryId(1);
//            return category1;
//        });
//        assertNotNull(categoryService.addCategory(category));
//        assertEquals(1,categoryService.addCategory(category).getCategoryId());
//        assertEquals("Horror", categoryService.addCategory(category).getCategoryName());
//
//    }
//
//    @Test
//    public void findByCategoryNameTest(){
//        Category category=new Category();
//        category.setCategoryName("Adventure");
//        when(categoryRepository.findBycategoryName(category.getCategoryName())).thenReturn(category);
//        assertNotNull(categoryService.findByCategoryName(category.getCategoryName()));
//        assertEquals("Adventure",categoryService.findByCategoryName(category.getCategoryName()).getCategoryName());
//    }
//
//    @Test
//    public void addBookCategoryTest(){
//        BookCategory bookCategory=new BookCategory();
//        bookCategory.setBookId(1);
//        bookCategory.setCategoryName("Horror");
//        when(bookCategoryRepository.save(bookCategory)).thenAnswer(invocation -> {
//            BookCategory bookCategory1=invocation.getArgument(0);
//            bookCategory1.setId(2);
//            return bookCategory1;
//        });
//        assertNotNull(categoryService.addBookCategory(bookCategory));
//        assertEquals(1,categoryService.addBookCategory(bookCategory).getBookId());
//        assertEquals("Horror",categoryService.addBookCategory(bookCategory).getCategoryName());
//        assertEquals(2,categoryService.addBookCategory(bookCategory).getId());
//    }






}

package com.target.ready.library.system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.entity.UserProfile;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.repository.BookCategoryRepository;
import com.target.ready.library.system.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
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

    @Test
    public void findAllCategoriesTest(){
        List<Category> myCategories = new ArrayList<Category>();

        myCategories.add(new Category(1,"Horror"));
        myCategories.add(new Category(2, "Adventure"));

        when(categoryRepository.findAllCategories(0,10)).thenReturn(myCategories);
        assertEquals(2,categoryService.findAllCategories(0,10).size());
    }

    @Test
    public void getTotalCategoryCountTest() {
        List<Category> myCategories = new ArrayList<Category>();

        myCategories.add(new Category(1,"Horror"));
        myCategories.add(new Category(2, "Adventure"));

        Mono<Long> repoCount = Mono.just(0L);
        when(categoryRepository.totalCategories()).thenReturn(repoCount);
        Mono<Long> serviceCount = categoryService.getTotalCategoryCount();
        Assertions.assertEquals(repoCount, serviceCount);
    }

    @Test
    public void findCategoryByCategoryNameTest() throws ResourceNotFoundException {
        String categoryName = "Fiction";
        Category mockCategory = new Category();
        mockCategory.setCategoryName(categoryName);
        when(categoryRepository.findCategoryBycategoryName(eq(categoryName.toLowerCase())))
                .thenReturn(mockCategory);
        Category result = categoryService.findCategoryBycategoryName(categoryName);
        verify(categoryRepository).findCategoryBycategoryName(categoryName.toLowerCase());
        assertNotNull(result);
        assertEquals(categoryName, result.getCategoryName());
    }

    @Test
    public void addCategoryTest() throws ResourceAlreadyExistsException, JsonProcessingException {
        Category newCategory = new Category();
        newCategory.setCategoryName("Fiction");

        when(categoryRepository.addCategory(eq(newCategory)))
                .thenReturn(newCategory);
        Category addedCategory = categoryService.addCategory(newCategory);

        verify(categoryRepository).addCategory(newCategory);

        assertNotNull(addedCategory);
        assertEquals(newCategory.getCategoryName(), addedCategory.getCategoryName());
    }

    @Test
    public void addBookCategoryTest() throws JsonProcessingException, ResourceAlreadyExistsException {
        BookCategory newBookCategory = new BookCategory();
        newBookCategory.setCategoryName("Fiction");
        when(bookCategoryRepository.addBookCategory(eq(newBookCategory)))
                .thenReturn(newBookCategory);
        BookCategory addedBookCategory = categoryService.addBookCategory(newBookCategory);
        verify(bookCategoryRepository).addBookCategory(newBookCategory);
        assertNotNull(addedBookCategory);
        assertEquals(newBookCategory.getCategoryName(), addedBookCategory.getCategoryName());
    }

    @Test
    public void deleteBookCategoryTest() throws ResourceNotFoundException {
        int bookId = 123;
        when(bookCategoryRepository.deleteBookCategory(eq(bookId)))
                .thenReturn("Deleted");
        String result = categoryService.deleteBookCategory(bookId);
        verify(bookCategoryRepository).deleteBookCategory(bookId);
        assertNotNull(result);
        assertEquals("Deleted", result);
    }







}

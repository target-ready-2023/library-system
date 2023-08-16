package com.target.ready.library.system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.repository.BookCategoryRepository;
import com.target.ready.library.system.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

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
    public void testFindCategoryByCategoryName() throws ResourceNotFoundException {
        String categoryName = "Fiction";
        Category mockCategory = new Category();
        mockCategory.setCategoryName(categoryName);

        // Mock the behavior of the repository
        when(categoryRepository.findCategoryBycategoryName(eq(categoryName.toLowerCase())))
                .thenReturn(mockCategory);

        // Call the service method
        Category result = categoryService.findCategoryBycategoryName(categoryName);

        // Verify the repository method was called with the correct argument
        verify(categoryRepository).findCategoryBycategoryName(categoryName.toLowerCase());

        // Verify the returned result
        assertNotNull(result);
        assertEquals(categoryName, result.getCategoryName());
    }

    @Test
    public void testAddCategory() throws ResourceAlreadyExistsException, JsonProcessingException {
        // Create a mock category to add
        Category newCategory = new Category();
        newCategory.setCategoryName("Fiction");

        // Mock the behavior of the repository
        when(categoryRepository.addCategory(eq(newCategory)))
                .thenReturn(newCategory); // You can adjust this as needed

        // Call the service method
        Category addedCategory = categoryService.addCategory(newCategory);

        // Verify the repository method was called with the correct argument
        verify(categoryRepository).addCategory(newCategory);

        // Verify the returned result
        assertNotNull(addedCategory);
        assertEquals(newCategory.getCategoryName(), addedCategory.getCategoryName());
    }

    @Test
    public void testAddBookCategory() throws JsonProcessingException, ResourceAlreadyExistsException {
        // Create a mock book category to add
        BookCategory newBookCategory = new BookCategory();
        newBookCategory.setCategoryName("Fiction");

        // Mock the behavior of the repository
        when(bookCategoryRepository.addBookCategory(eq(newBookCategory)))
                .thenReturn(newBookCategory); // You can adjust this as needed

        // Call the service method
        BookCategory addedBookCategory = categoryService.addBookCategory(newBookCategory);

        // Verify the repository method was called with the correct argument
        verify(bookCategoryRepository).addBookCategory(newBookCategory);

        // Verify the returned result
        assertNotNull(addedBookCategory);
        assertEquals(newBookCategory.getCategoryName(), addedBookCategory.getCategoryName());
    }

    @Test
    public void testDeleteBookCategory() throws ResourceNotFoundException {
        int bookId = 123; // Replace with a valid book ID

        // Mock the behavior of the repository
        when(bookCategoryRepository.deleteBookCategory(eq(bookId)))
                .thenReturn("Deleted"); // You can adjust this as needed

        // Call the service method
        String result = categoryService.deleteBookCategory(bookId);

        // Verify the repository method was called with the correct argument
        verify(bookCategoryRepository).deleteBookCategory(bookId);

        // Verify the returned result
        assertNotNull(result);
        assertEquals("Deleted", result);
    }


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

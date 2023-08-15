package com.target.ready.library.system.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.dto.BookDto;
import com.target.ready.library.system.dto.BookDtoUpdate;
import com.target.ready.library.system.entity.*;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.repository.BookRepository;
import com.target.ready.library.system.repository.InventoryRepository;
import com.target.ready.library.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service

public class LibrarySystemService {

    private final CategoryService categoryService;

    private final BookRepository bookRepository;

    private final InventoryRepository inventoryRepository;

    private final UserRepository userRepository;

    @Autowired
    public LibrarySystemService(BookRepository bookRepository, InventoryRepository inventoryRepository, UserRepository userRepository, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
        this.categoryService = categoryService;
    }


    public List<Book> getAllBooks(int pageNumber, int pageSize) {
        return bookRepository.getAllBooks(pageNumber, pageSize);
    }

    public Mono<Long> getTotalBookCount() {
        return bookRepository.totalBooks();
    }


    public Book findByBookId(int bookId) {
        Book book = bookRepository.findByBookId(bookId);
        return book;
    }


    @Transactional
    public BookDto addBook(BookDto bookDto) throws ResourceAlreadyExistsException, JsonProcessingException {

        String lowercaseCategoryName;
        Book book = bookRepository.addBook(bookDto);

        int bookId = book.getBookId();
        int noOfCopies = bookDto.getNoOfCopies();
        Inventory inventory = new Inventory();
        inventory.setInvBookId(bookId);
        inventory.setNoOfCopies(noOfCopies);
        inventory.setNoOfBooksLeft(noOfCopies);
        Inventory inventory1 = inventoryRepository.addInventory(inventory);
        List<String> categoryNames = bookDto.getCategoryNames();
        List<String> savedCategoryNames = new ArrayList<>();
        for (String eachCategoryName : categoryNames) {
            lowercaseCategoryName = eachCategoryName.trim().toLowerCase(); // Convert to lowercase
            try {

                savedCategoryNames.add(lowercaseCategoryName);
                Category category = categoryService.findCategoryBycategoryName(
                        lowercaseCategoryName
                );
            } catch (ResourceNotFoundException ex) {

                Category category1 = new Category();
                category1.setCategoryName(lowercaseCategoryName);
                categoryService.addCategory(category1);

            }


            BookCategory bookCategory = new BookCategory();
            bookCategory.setBookId(bookId);
            bookCategory.setCategoryName(lowercaseCategoryName);
            categoryService.addBookCategory(bookCategory);
        }
        BookDto savedBookDto = new BookDto();
        bookDto.setBook(book);
        bookDto.setCategoryNames(savedCategoryNames);
        bookDto.setNoOfCopies(inventory1.getNoOfBooksLeft());
        return bookDto;

    }

    public List<Book> findBookByCategoryName(String categoryName, int pageNumber, int pageSize) throws ResourceNotFoundException {
        List<Book> bookList = bookRepository.findBookByCategoryName(categoryName.toLowerCase(), pageNumber, pageSize);
        return bookList;
    }

    public Mono<Long> getTotalBookCategoryCount(String categoryName) {
        return bookRepository.countBooksByCategoryName(categoryName.toLowerCase());
    }

    public List<Book> findByBookName(String bookName) {
        List<Book> books = bookRepository.findByBookName(bookName);
        return books;
    }

    public String deleteBook(int bookId) throws ResourceNotFoundException, DataAccessException{
        try {
            bookRepository.deleteBook(bookId);
            categoryService.deleteBookCategory(bookId);
            return "Book deleted Successfully!";
        } catch (DataAccessException ex)
        {
            throw ex;
        }
        catch(ResourceNotFoundException ex){
            throw ex;
        }
    }

    @Transactional
    public BookDtoUpdate updateBookDetails(int bookId, BookDtoUpdate bookDtoUpdate) throws JsonProcessingException, ResourceAlreadyExistsException {
        String lowerCategoryName;
        List<String> updatedBookCategories = new ArrayList<>();
        BookDtoUpdate updatedBookDto = new BookDtoUpdate();
        updatedBookDto.setBook(bookRepository.updateBookDetails(bookId, bookDtoUpdate));
        categoryService.deleteBookCategory(bookId);
        List<String> categoryNames = bookDtoUpdate.getCategoryNames();

        for (String categoryName : categoryNames) {
            lowerCategoryName = categoryName.trim().toLowerCase();
            try {
                updatedBookCategories.add(lowerCategoryName);
                Category category = categoryService.findCategoryBycategoryName(lowerCategoryName);
            } catch (ResourceNotFoundException ex) {
                Category category1 = new Category();
                category1.setCategoryName(lowerCategoryName);
                categoryService.addCategory(category1);
            }
            BookCategory bookCategory = new BookCategory();
            bookCategory.setBookId(bookId);
            bookCategory.setCategoryName(lowerCategoryName);
            categoryService.addBookCategory(bookCategory);
        }
        updatedBookDto.setCategoryNames(updatedBookCategories);
        return updatedBookDto;
    }

    public String booksIssued(int bookId, int userId) throws ResourceNotFoundException, ResourceAlreadyExistsException {


        UserCatalog userCatalog = new UserCatalog();
        userCatalog.setBookId(bookId);
        userCatalog.setUserId(userId);
        userRepository.addUserCatalog(userCatalog);
        Inventory inventory = inventoryRepository.findByBookId(bookId);
        inventory.setNoOfBooksLeft(inventory.getNoOfBooksLeft() - 1);
        inventoryRepository.addInventory(inventory);
        return "Book issued";

    }

    public Integer bookReturned(int bookId, int userId) {
        Integer returnedBookId = 0;
        Integer flag=0;
        List<UserCatalog> userCatalogs = userRepository.findBooksByUserId(userId);
        List<Integer> bookIds = new ArrayList<>();
        for (UserCatalog eachUserCatalog : userCatalogs) {
            int bookId1 = eachUserCatalog.getBookId();
            bookIds.add(bookId1);
        }

        for (Integer eachBookId : bookIds) {
            if (eachBookId == bookId) {
                flag=1;
                Inventory inventory = inventoryRepository.findByBookId(bookId);
                inventory.setNoOfBooksLeft(inventory.getNoOfBooksLeft() + 1);
                inventoryRepository.addInventory(inventory);
                returnedBookId = userRepository.deleteBookByUserId(bookId, userId);

            }
        }
        if(flag==0){
            throw new ResourceNotFoundException("Student doesn't have this book");
        }
        //return "Book Returned Successfully";
        return returnedBookId;
    }


    public Integer getNoOfCopiesByBookId(Integer bookId) throws ResourceNotFoundException {
        Inventory inventory = inventoryRepository.findByBookId(bookId);
        Integer noOfCopies = inventory.getNoOfBooksLeft();
        return noOfCopies;
    }

    public Inventory findByBookId(Integer bookId) throws ResourceNotFoundException {
        return inventoryRepository.findByBookId(bookId);
    }
}





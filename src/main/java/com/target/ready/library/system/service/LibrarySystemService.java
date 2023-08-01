package com.target.ready.library.system.service;


import com.target.ready.library.system.dto.BookDto;
import com.target.ready.library.system.entity.*;
import com.target.ready.library.system.repository.BookRepository;
import com.target.ready.library.system.repository.InventoryRepository;
import com.target.ready.library.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class LibrarySystemService {

    private final CategoryService categoryService;

    private final BookRepository bookRepository;


    private final InventoryRepository inventoryRepository;


    private final UserRepository userRepository;

    @Autowired
    public LibrarySystemService(BookRepository bookRepository, InventoryRepository inventoryRepository, UserRepository userRepository, CategoryService categoryService){
        this.bookRepository = bookRepository;
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
        this.categoryService = categoryService;
    }


    public List<Book> getAllBooks(int pageNumber, int pageSize) {
        return bookRepository.getAllBooks(pageNumber,pageSize);
    }


    public Book findByBookId(int bookId) {
        Book book = bookRepository.findByBookId(bookId);
        return book;
    }


    @Transactional
    public String addBook(BookDto bookDto) {
        Book book = bookRepository.addBook(bookDto);
        int bookId = book.getBookId();
        int noOfCopies=bookDto.getNoOfCopies();
        Inventory inventory=new Inventory();
        inventory.setInvBookId(bookId);
        inventory.setNoOfCopies(noOfCopies);
        inventory.setNoOfBooksLeft(noOfCopies);
        inventoryRepository.addInventory(inventory);
        List<String> categoryNames = bookDto.getCategoryNames();
        for (String eachCategoryName : categoryNames) {

            Category category = categoryService.findCategoryBycategoryName(eachCategoryName);
            if (category == null) {
                Category category1 = new Category();
                category1.setCategoryName(eachCategoryName);
                categoryService.addCategory(category1);
            }

            BookCategory bookCategory = new BookCategory();
            bookCategory.setBookId(bookId);
            bookCategory.setCategoryName(eachCategoryName);
            categoryService.addBookCategory(bookCategory);
        }
        return "Book Added Successfully";
    }

    public List<Book> findBookByCategoryName(String categoryName) {
        List<Book> bookList= bookRepository.findBookByCategoryName(categoryName);
        return bookList;
    }

    public List<Book> findByBookName(String bookName){
       List<Book> books = bookRepository.findByBookName(bookName);
       return  books;
    }

    public String deleteBook(int bookId) {
        try {
            bookRepository.deleteBook(bookId);
            categoryService.deleteBookCategory(bookId);
            return "Book deleted Successfully!";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete book", e);
        }
    }


    @Transactional
    public String updateBookDetails(int bookId, BookDto bookDto) {
        try {
            bookRepository.updateBookDetails(bookId, bookDto);
            categoryService.deleteBookCategory(bookId);
            List<String> categoryNames = bookDto.getCategoryNames();
            for (String categoryName : categoryNames) {
                Category category1 = new Category();
                category1.setCategoryName(categoryName);
                categoryService.addCategory(category1);
                BookCategory bookCategory = new BookCategory();
                bookCategory.setBookId(bookId);
                bookCategory.setCategoryName(categoryName);
                categoryService.addBookCategory(bookCategory);
            }
            return "Book updated" ;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update book details", e);
        }
    }

    public String booksIssued(int bookId,int userId){

       Inventory inventory= inventoryRepository.findByBookId(bookId);
       inventory.setNoOfBooksLeft(inventory.getNoOfBooksLeft()-1);
       inventoryRepository.addInventory(inventory);
        UserCatalog userCatalog =new UserCatalog();
        userCatalog.setBookId(bookId);
        userCatalog.setUserId(userId);
        userRepository.addUserCatalog(userCatalog);
        return "Book issued";

    }

    public Integer bookReturned(int bookId,int userId){
        Integer returnedBookId = 0;
        List<Integer> bookIdList=userRepository.findBooksByUserId(userId);
        for(Integer eachBookId:bookIdList){
            if(eachBookId==bookId){
                Inventory inventory= inventoryRepository.findByBookId(bookId);
                inventory.setNoOfBooksLeft(inventory.getNoOfBooksLeft()+1);
                inventoryRepository.addInventory(inventory);
                returnedBookId = userRepository.deleteBookByUserId(bookId,userId);
            }
        }
        //return "Book Returned Successfully";
        return returnedBookId;
    }



}





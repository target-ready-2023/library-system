package com.target.ready.library.system.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.dto.BookDto;
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
    public LibrarySystemService(BookRepository bookRepository, InventoryRepository inventoryRepository, UserRepository userRepository, CategoryService categoryService){
        this.bookRepository = bookRepository;
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
        this.categoryService = categoryService;
    }


    public List<Book> getAllBooks(int pageNumber, int pageSize) {
        return bookRepository.getAllBooks(pageNumber,pageSize);
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
            Inventory inventory1=inventoryRepository.addInventory(inventory);
            List<String> categoryNames = bookDto.getCategoryNames();
            List<String> savedCategoryNames=new ArrayList<>();
            for (String eachCategoryName : categoryNames) {
                lowercaseCategoryName = eachCategoryName.trim().toLowerCase(); // Convert to lowercase
                try {

                    savedCategoryNames.add(lowercaseCategoryName);
                    Category category = categoryService.findCategoryBycategoryName(
                            lowercaseCategoryName
                    );
                }catch(ResourceNotFoundException ex){

                        Category category1 = new Category();
                        category1.setCategoryName(lowercaseCategoryName);
                        categoryService.addCategory(category1);

                }


                BookCategory bookCategory = new BookCategory();
                bookCategory.setBookId(bookId);
                bookCategory.setCategoryName(lowercaseCategoryName);
                categoryService.addBookCategory(bookCategory);
            }
            BookDto savedBookDto=new BookDto();
            bookDto.setBook(book);
            bookDto.setCategoryNames(savedCategoryNames);
            bookDto.setNoOfCopies(inventory1.getNoOfBooksLeft());
            return bookDto;

    }

    public List<Book> findBookByCategoryName(String categoryName,int pageNumber,int pageSize) {
        List<Book> bookList= bookRepository.findBookByCategoryName(categoryName.toLowerCase(),pageNumber,pageSize);
        return bookList;
    }

    public Mono<Long> getTotalBookCategoryCount(String categoryName) {
        return bookRepository.countBooksByCategoryName(categoryName.toLowerCase());
    }

    public List<Book> findByBookName(String bookName){
       List<Book> books = bookRepository.findByBookName(bookName);
       return  books;
    }

    public String deleteBook(int bookId) throws ResourceNotFoundException, DataAccessException{
        try {
            bookRepository.deleteBook(bookId);
            categoryService.deleteBookCategory(bookId);
            return "Book deleted Successfully!";
        } catch(Exception ex){
            throw ex;
        }
    }


    @Transactional
    public String updateBookDetails(int bookId, BookDto bookDto) {
        String lowerCategoryName;
        try {
            bookRepository.updateBookDetails(bookId, bookDto);
            categoryService.deleteBookCategory(bookId);
            List<String> categoryNames = bookDto.getCategoryNames();

            for (String categoryName : categoryNames) {
                lowerCategoryName = categoryName.trim().toLowerCase();
                Category category = categoryService.findCategoryBycategoryName(lowerCategoryName);
                if (category == null) {
                    Category category1 = new Category();
                    category1.setCategoryName(lowerCategoryName);
                    categoryService.addCategory(category1);
                }
                BookCategory bookCategory = new BookCategory();
                bookCategory.setBookId(bookId);
                bookCategory.setCategoryName(lowerCategoryName);
                categoryService.addBookCategory(bookCategory);
            }
            return "Book updated" ;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update book details", e);
        }
    }

    public String booksIssued(int bookId,int userId) throws ResourceNotFoundException,ResourceAlreadyExistsException{

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


    public Integer getNoOfCopiesByBookId(Integer bookId) throws ResourceNotFoundException{
        Inventory inventory=inventoryRepository.findByBookId(bookId);
        Integer noOfCopies=inventory.getNoOfBooksLeft();
        return noOfCopies;
    }

    public Inventory findByBookId(Integer bookId) throws ResourceNotFoundException{
        return inventoryRepository.findByBookId(bookId);
    }
}





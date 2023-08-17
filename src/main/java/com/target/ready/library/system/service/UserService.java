package com.target.ready.library.system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.UserCatalog;
import com.target.ready.library.system.entity.UserProfile;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.repository.BookRepository;
import com.target.ready.library.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;
    public UserProfile addUser(UserProfile userProfile) throws JsonProcessingException, ResourceAlreadyExistsException {
        return userRepository.addUser(userProfile);}

    public String deleteUser(int userId) throws ResourceAlreadyExistsException,ResourceNotFoundException{

            return userRepository.deleteUser(userId);
    }


    public List<UserProfile> getAllUsers(int pageNumber,int pageSize) throws ResourceNotFoundException{
        return userRepository.getAllUsers(pageNumber,pageSize);
    }

    public List<UserProfile> fetchAllUsers() throws ResourceNotFoundException{
        return userRepository.fetchAllUsers();
    }
    public Mono<Long> getTotalUsersCount() {
        return userRepository.totalUsers();
    }
    public List<String> findBookNameByUserId(int userId) throws ResourceNotFoundException{
        List<String> bookNames = new ArrayList<>();
        List<UserCatalog> userCatalogs = userRepository.findBooksByUserId(userId);
        System.out.println(userCatalogs);
        List<Integer> bookIds = new ArrayList<>();
        for (UserCatalog eachUserCatalog : userCatalogs) {
            int bookId1 = eachUserCatalog.getBookId();
            bookIds.add(bookId1);
        }

        for (int bookId : bookIds) {
            Book book = bookRepository.findByBookId(bookId);
            System.out.println(book);
            if (book != null) {
                bookNames.add(book.getBookName());
            }
        }

        return bookNames;
    }

}

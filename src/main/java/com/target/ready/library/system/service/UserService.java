package com.target.ready.library.system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.UserProfile;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.repository.BookRepository;
import com.target.ready.library.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public String deleteUser(int userId) {

        if(userRepository.findBooksByUserId(userId).size()>0){
            return "User has books checked out. Cannot delete user";
        }
            return userRepository.deleteUser(userId);
    }


    public List<UserProfile> getAllUsers() throws ResourceNotFoundException{
        return userRepository.getAllUsers();
    }

    public List<String> findBookNameByUserId(int userId) {
        List<String> bookNames = new ArrayList<>();
        List<Integer> bookIds = userRepository.findBooksByUserId(userId);

        for (int bookId : bookIds) {
            Book book = bookRepository.findByBookId(bookId);
            if (book != null) {
                bookNames.add(book.getBookName());
            }
        }

        return bookNames;
    }

}

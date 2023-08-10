package com.target.ready.library.system.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.entity.UserProfile;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.repository.BookCategoryRepository;
import com.target.ready.library.system.repository.CategoryRepository;
import com.target.ready.library.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public UserProfile addUser(UserProfile userProfile) throws JsonProcessingException, ResourceAlreadyExistsException {
        return userRepository.addUser(userProfile);}

    public String deleteUser(int userId) {

        if(userRepository.findBooksByUserId(userId).size()>0){
            return "User has books checked out. Cannot delete";
        }
            return userRepository.deleteUser(userId);
    }


}

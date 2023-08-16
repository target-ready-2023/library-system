package com.target.ready.library.system.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.UserCatalog;
import com.target.ready.library.system.entity.UserProfile;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;

import java.util.List;

public interface UserRepository {

    public List<UserCatalog> findBooksByUserId(int userId);
    public Integer deleteBookByUserId(int bookId,int userId);

    public UserCatalog addUserCatalog(UserCatalog userCatalog) throws ResourceAlreadyExistsException, ResourceNotFoundException,JsonProcessingException;
    public UserProfile addUser(UserProfile userProfile) throws JsonProcessingException, ResourceAlreadyExistsException;

    public UserProfile findByUserId(int userId);

    String deleteUser(int userId);

    public List<UserProfile> getAllUsers() throws ResourceNotFoundException;
}

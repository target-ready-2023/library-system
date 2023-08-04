package com.target.ready.library.system.repository;

import com.target.ready.library.system.entity.UserCatalog;
import com.target.ready.library.system.entity.UserProfile;

import java.util.List;

public interface UserRepository {

    public List<Integer> findBooksByUserId(int userId);
    public Integer deleteBookByUserId(int bookId,int userId);

    public UserCatalog addUserCatalog(UserCatalog userCatalog);
    public UserProfile addUser(UserProfile userProfile);
}

package com.target.ready.library.system.repository;

import com.target.ready.library.system.entity.Inventory;
import com.target.ready.library.system.entity.User;

import java.util.List;

public interface UserRepository {

    public List<Integer> findBooksByUserId(int userId);
    public void deleteBookByUserId(int bookId,int userId);

    public User addUser(User user);
}

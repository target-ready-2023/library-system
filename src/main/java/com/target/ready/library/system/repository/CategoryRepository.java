package com.target.ready.library.system.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.exceptions.ClientErrorException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;

import java.util.List;


public interface CategoryRepository {

    public Category findCategoryBycategoryName(String categoryName) throws ResourceNotFoundException;
    public List<Category> findAllCategories(int pageNumber, int pageSize) ;
    public void addCategory(Category category) throws ClientErrorException, JsonProcessingException;
}

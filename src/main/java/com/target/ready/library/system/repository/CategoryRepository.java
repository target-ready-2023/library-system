package com.target.ready.library.system.repository;

import com.target.ready.library.system.entity.Category;

import java.util.List;


public interface CategoryRepository {

    public Category findCategoryBycategoryName(String categoryName);
    public List<Category> findAllCategories(int pageNumber, int pageSize);
    public void addCategory(Category category);
}

package com.target.ready.library.system.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.Entity.Category;
import com.target.ready.library.system.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("library_system/v2")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("inventory/category")
    public String addCategory(@RequestBody Category category) throws JsonProcessingException {
        return categoryService.addCategory(category);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories(){
        return categoryService.findAllCategories();
    }
}

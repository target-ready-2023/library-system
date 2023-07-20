package com.target.ready.library.system.Controller;

import com.target.ready.library.system.Entity.Category;
import com.target.ready.library.system.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("library_system/v2")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/inventory/category")
    public String addCategory(Category category){
        return categoryService.addCategory(category);
    }
}

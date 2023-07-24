package com.target.ready.library.system.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.Entity.Category;
import com.target.ready.library.system.Service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("library_system/v2")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("inventory/category")
    @Operation(
            description = "Addition of categories",
            responses = { @ApiResponse(
            responseCode = "201",
            description = "Category Successfully added!",
            content = @Content(
                    mediaType = "application/json"
            ))})
    public String addCategory(@RequestBody Category category) throws JsonProcessingException {
        return categoryService.addCategory(category);
    }

    @GetMapping("/categories")
    @Operation(
            description = "Gives all the books added in the database",
            responses = { @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public List<Category> getAllCategories(){
        return categoryService.findAllCategories();
    }
}

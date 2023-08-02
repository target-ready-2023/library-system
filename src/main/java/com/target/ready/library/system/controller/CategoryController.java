package com.target.ready.library.system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("library_system/v2")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("inventory/category")
    @Operation(
            description = "Addition of new categories",
            responses = { @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json"
            ))})
    public String addCategory(@RequestBody Category category) throws JsonProcessingException {
        return categoryService.addCategory(category);
    }

//    @GetMapping("categories")
//    @Operation(
//            description = "Addition of books and its details",
//            responses = { @ApiResponse(
//                    responseCode = "200",
//                    content = @Content(
//                            mediaType = "application/json"
//                    ))})
//    public ResponseEntity<List<Category>> getAllCategories(){
//        List<Category> categories = categoryService.findAllCategories();
//        return new ResponseEntity<>(categories, HttpStatus.OK);
//    }

    @GetMapping("/categories")
    @Operation(
            description = "Addition of books and its details",
            responses = { @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<List<Category>> findAllCategories(@RequestParam(value = "page_number", defaultValue = "0", required = false) Integer pageNumber) {
        List<Category> categories;
        int pageSize = 10;
        try {
            if (pageNumber < 0) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
            }
            categories = categoryService.findAllCategories(pageNumber, pageSize);
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
    }

    @GetMapping("categories/{bookId}")
    public ResponseEntity<List<BookCategory>> findAllCategoriesByBookId(@PathVariable int bookId) {
        List<BookCategory> categories = categoryService.findAllCategoriesByBookId(bookId);

        if (categories.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categories);
    }
}

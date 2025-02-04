package com.avnish.to_do_app.controller;

import com.avnish.to_do_app.entity.Category;
import com.avnish.to_do_app.entity.CategoryDTO;
import com.avnish.to_do_app.exception.CategoryNotFoundException;
import com.avnish.to_do_app.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/v1")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategory(){
        return categoryService.getAllCategories();
    }

    @DeleteMapping("/category")
    public ResponseEntity<String> deleteCategory(@RequestParam("cid") long id){
        return categoryService.deleteCategory(id);
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(categoryDTO));
    }

    @GetMapping("category/{id}")
    public ResponseEntity<Category> categoryById(@PathVariable long id) throws CategoryNotFoundException {
        return ResponseEntity.ok(categoryService.categoryById(id));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String,String>> handler(CategoryNotFoundException ex){
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        errorMap.put("status", HttpStatus.NOT_FOUND.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
    }
}

package gomarketapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gomarketapi.helpers.ResponseBuilder;
import gomarketapi.model.CategoryBean;
import gomarketapi.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody CategoryBean categoryBody) throws Exception {
    CategoryBean category = this.categoryService.create(categoryBody);
    return ResponseBuilder.success("Category created sucessfully!", category);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'getById'");
  }

  @GetMapping
  public ResponseEntity<?> getAll(@RequestParam(required = false) String where) throws Exception {
    List<CategoryBean> list = this.categoryService.getAll(where);
    return ResponseBuilder.success(list);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CategoryBean entity) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }

}
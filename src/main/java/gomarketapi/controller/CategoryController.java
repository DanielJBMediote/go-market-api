package gomarketapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public CategoryBean create(@RequestBody CategoryBean categoryBody) throws Exception {
    return this.categoryService.create(categoryBody);
  }

  @GetMapping("/{id}")
  public CategoryBean getById(@PathVariable Long id) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'getById'");
  }

  @GetMapping
  public List<CategoryBean> getAll(@RequestParam(required = false) String where) throws Exception {
    return this.categoryService.getAll(where);
  }

  @PutMapping("/{id}")
  public CategoryBean update(@PathVariable Long id, @RequestBody CategoryBean entity) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }

}
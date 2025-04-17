package gomarketapi.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import gomarketapi.service.CategoryService;

@Component
public class DataLoader implements CommandLineRunner {

  private final CategoryService categoryService;

  public DataLoader(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Override
  public void run(String... args) throws Exception {
    categoryService.loadCategories();

  }

}

package gomarketapi.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import gomarketapi.model.CategoryBean;
import gomarketapi.repository.CategoryRepository;

@Component
public class DataLoader implements CommandLineRunner {

  private final CategoryRepository categoryRepository;

  public DataLoader(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public void run(String... args) throws Exception {

    if (categoryRepository.count() == 0) {

      categoryRepository.save(new CategoryBean("Electronics", "Smarthphones"));
      categoryRepository.save(new CategoryBean("Electronics", "Computers and Laptops"));
      categoryRepository.save(new CategoryBean("Electronics", "For Home"));
      categoryRepository.save(new CategoryBean("Electronics", "Accessories"));
      categoryRepository.save(new CategoryBean("Electronics", "Toys"));

      categoryRepository.save(new CategoryBean("Clothes", "Tops"));
      categoryRepository.save(new CategoryBean("Clothes", "Bottoms"));
      categoryRepository.save(new CategoryBean("Clothes", "Dresses"));
      categoryRepository.save(new CategoryBean("Clothes", "Accessories"));
      categoryRepository.save(new CategoryBean("Clothes", "Footwear"));

      System.out.println("Dados iniciais carregados com sucesso!");
    }

    // throw new UnsupportedOperationException("Unimplemented method 'run'");

  }

}

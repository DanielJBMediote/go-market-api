package gomarketapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import gomarketapi.helpers.WhereBuilder;
import gomarketapi.model.CategoryBean;
import gomarketapi.repository.CategoryRepository;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public CategoryBean create(CategoryBean category) {
    return this.categoryRepository.save(category);
  }

  public List<CategoryBean> getAll(String whereEncodedURI) {
    WhereBuilder<CategoryBean> whereBuilder = new WhereBuilder<CategoryBean>(CategoryBean.class);
    Specification<CategoryBean> spec = whereBuilder.parseWhereEncodedURIParams(whereEncodedURI).build();

    return this.categoryRepository.findAll(spec);
  }

  public void loadCategories() {

    List<CategoryBean> defaultList = new ArrayList<>();

    defaultList.add(new CategoryBean("Electronics", "Smarthphones", "ELEC1"));
    defaultList.add(new CategoryBean("Electronics", "Computers and Laptops", "ELEC2"));
    defaultList.add(new CategoryBean("Electronics", "For Home", "ELEC3"));
    defaultList.add(new CategoryBean("Electronics", "Accessories", "ELEC4"));
    defaultList.add(new CategoryBean("Electronics", "Toys", "ELEC5"));

    defaultList.add(new CategoryBean("Clothes", "Tops", "CLOT1"));
    defaultList.add(new CategoryBean("Clothes", "Bottoms", "CLOT2"));
    defaultList.add(new CategoryBean("Clothes", "Dresses", "CLOT3"));
    defaultList.add(new CategoryBean("Clothes", "Accessories", "CLOT4"));
    defaultList.add(new CategoryBean("Clothes", "Footwear", "CLOT5"));

    List<CategoryBean> existingCategories = categoryRepository.findAll();
    Set<String> existingCodes = existingCategories.stream()
        .map(CategoryBean::getCode)
        .collect(Collectors.toSet());

    List<CategoryBean> categoriesToInsert = defaultList.stream()
        .filter(cat -> !existingCodes.contains(cat.getCode()))
        .collect(Collectors.toList());

    if (!categoriesToInsert.isEmpty()) {
      categoryRepository.saveAll(categoriesToInsert);
      System.out.println("Found " + categoriesToInsert.size() + " to insert...");
      System.out.println("Default categories has been updated.");
    } else {
      System.out.println("All default categories are already updated..");
    }

    System.out.println("Default data loaded sucessfully!");
  }
}

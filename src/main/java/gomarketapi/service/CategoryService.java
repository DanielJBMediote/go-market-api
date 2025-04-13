package gomarketapi.service;

import java.util.List;

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
}

package gomarketapi.dto.products;

import java.math.BigDecimal;

import gomarketapi.dto.store.StoreReadDTO;
import gomarketapi.model.CategoryBean;

public class ProductCreateDTO {

  private String name;
  private String description;

  private Boolean isFeatured;

  private BigDecimal price;

  private StoreReadDTO store;
  private CategoryBean category;

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Boolean getIsFeatured() {
    return isFeatured;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public StoreReadDTO getStore() {
    return store;
  }

  public CategoryBean getCategory() {
    return category;
  }

}

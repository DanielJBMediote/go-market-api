package gomarketapi.dto.products;

import java.math.BigDecimal;

import gomarketapi.dto.StoreDTO;
import gomarketapi.model.CategoryBean;

public class ProductCreateDTO {

  private String name;
  private String description;

  private Boolean isFeatured;

  private BigDecimal price;

  private StoreDTO store;
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

  public StoreDTO getStore() {
    return store;
  }

  public CategoryBean getCategory() {
    return category;
  }

}

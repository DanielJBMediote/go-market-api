package gomarketapi.dto.products;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import gomarketapi.dto.products.comments.ProductCommentReadDTO;
import gomarketapi.dto.store.StoreReadDTO;
import gomarketapi.mapper.ProductMapper;
import gomarketapi.model.CategoryBean;
import gomarketapi.model.ProductBean;
import gomarketapi.model.ProductFileBean;

public class ProductReadDTO {

  private Long id;
  private String name;
  private String description;

  private Boolean isActive;
  private Boolean isFeatured;

  private Integer likes;
  private Integer timesOrdered;

  private BigDecimal price;
  private BigDecimal lowestPrice;
  private BigDecimal highestPrice;

  private StoreReadDTO store;
  private CategoryBean category;
  private List<ProductFileBean> images;
  private List<ProductCommentReadDTO> comments;
  private List<Long> remainingImageIds;

  public ProductReadDTO(Long id) {
    this.id = id;
  }

  public ProductReadDTO() {
    this.images = new ArrayList<>();
  }

  public ProductReadDTO(ProductBean prodBean) {
    this.id = prodBean.getId();
    this.category = prodBean.getCategory();
    this.name = prodBean.getName();
    this.description = prodBean.getDescription();
    this.isActive = prodBean.getIsActive();
    this.isFeatured = prodBean.getIsFeatured();
    this.likes = prodBean.getLikes();
    this.store = new StoreReadDTO(prodBean.getStore());
    this.price = prodBean.getPrice();
    this.lowestPrice = prodBean.getLowestPrice();
    this.highestPrice = prodBean.getHighestPrice();
    this.images = prodBean.getImages();
    this.comments = ProductMapper.mapCommentsToDTO(prodBean.getComments());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Boolean getIsFeatured() {
    return isFeatured;
  }

  public void setIsFeatured(Boolean isFeatured) {
    this.isFeatured = isFeatured;
  }

  public Integer getLikes() {
    return likes;
  }

  public void setLikes(Integer likes) {
    this.likes = likes;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getLowestPrice() {
    return lowestPrice;
  }

  public void setLowestPrice(BigDecimal lowestPrice) {
    this.lowestPrice = lowestPrice;
  }

  public BigDecimal getHighestPrice() {
    return highestPrice;
  }

  public void setHighestPrice(BigDecimal highestPrice) {
    this.highestPrice = highestPrice;
  }

  public StoreReadDTO getStore() {
    return store;
  }

  public void setStore(StoreReadDTO store) {
    this.store = store;
  }

  public CategoryBean getCategory() {
    return category;
  }

  public void setCategory(CategoryBean category) {
    this.category = category;
  }

  public List<ProductFileBean> getImages() {
    return images;
  }

  public void setImages(List<ProductFileBean> images) {
    this.images = images;
  }

  public Integer getTimesOrdered() {
    return timesOrdered;
  }

  public void setTimesOrdered(Integer timesOrdered) {
    this.timesOrdered = timesOrdered;
  }

  public List<ProductCommentReadDTO> getComments() {
    return comments;
  }

  public void setComments(List<ProductCommentReadDTO> comments) {
    this.comments = comments;
  }

  public List<Long> getRemainingImageIds() {
    return remainingImageIds;
  }

}

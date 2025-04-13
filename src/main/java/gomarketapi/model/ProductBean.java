package gomarketapi.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductBean extends Bean {

  private String name;
  private String description;

  private Boolean isActive = true;
  private Boolean isFeatured = false;

  private BigDecimal price;
  private BigDecimal lowestPrice = BigDecimal.ZERO;
  private BigDecimal highestPrice = BigDecimal.ZERO;

  private Integer likes = 0;
  private Integer timesOrdered = 0;

  @ManyToOne
  @JoinColumn(name = "store_id", referencedColumnName = "id")
  private StoreBean store;

  @ManyToOne
  @JoinColumn(name = "category_id", referencedColumnName = "id")
  private CategoryBean category;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<ProductFileBean> images;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<ProductCommentBean> comments;

  public ProductBean() {
  }

  public ProductBean(Long id) {
    super(id);
  }

  public ProductBean(String name, String description, BigDecimal price) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.images = new ArrayList<>();
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

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getLowestPrice() {
    return lowestPrice;
  }

  /**
   * If not pass any value, it will be BigDecimal.ZERO
   * 
   * @param lowestPrice
   */
  public void setLowestPrice(BigDecimal lowestPrice) {
    this.lowestPrice = lowestPrice;
  }

  public BigDecimal getHighestPrice() {
    return highestPrice;
  }

  /**
   * If not pass any value, it will be BigDecimal.ZERO
   * 
   * @param highestPrice
   */
  public void setHighestPrice(BigDecimal highestPrice) {
    this.highestPrice = highestPrice;
  }

  public Integer getLikes() {
    return likes;
  }

  public void setLikes(Integer likes) {
    this.likes = likes;
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

  public StoreBean getStore() {
    return store;
  }

  public void setStore(StoreBean store) {
    this.store = store;
  }

  public Integer getTimesOrdered() {
    return timesOrdered;
  }

  public void setTimesOrdered(Integer timesOrdered) {
    this.timesOrdered = timesOrdered;
  }

  public List<ProductCommentBean> getComments() {
    return comments;
  }

  public void setComments(List<ProductCommentBean> comments) {
    this.comments = comments;
  }

}

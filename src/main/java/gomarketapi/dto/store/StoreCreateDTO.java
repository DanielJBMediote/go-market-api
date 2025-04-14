package gomarketapi.dto.store;

import gomarketapi.model.FileBean;

public class StoreCreateDTO {

  private String name;
  private String slug;
  private String description;
  private Boolean isActive;
  private Integer likes;
  private FileBean logo;

  public StoreCreateDTO() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
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

  public Integer getLikes() {
    return likes;
  }

  public void setLikes(Integer likes) {
    this.likes = likes;
  }

  public FileBean getLogo() {
    return logo;
  }

  public void setLogo(FileBean logo) {
    this.logo = logo;
  }

}

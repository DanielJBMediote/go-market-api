package gomarketapi.dto;

import java.time.LocalDateTime;

import gomarketapi.mapper.UserMapper;
import gomarketapi.model.FileBean;
import gomarketapi.model.StoreBean;

public class StoreDTO {

  private Long id;
  private String name;
  private String slug;
  private String description;
  private Boolean isActive;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Integer likes;
  private UserDTO owner;
  private FileBean logo;

  public StoreDTO() {

  }

  public StoreDTO(StoreBean storeBean) {
    this.id = storeBean.getId();
    this.name = storeBean.getName();
    this.slug = storeBean.getSlug();
    this.description = storeBean.getDescription();
    this.owner = UserMapper.toDTO(storeBean.getOwner());
    this.isActive = storeBean.getIsActive();
    this.logo = storeBean.getLogo();
    this.likes = storeBean.getLikes();
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

  public UserDTO getOwner() {
    return owner;
  }

  public void setOwner(UserDTO owner) {
    this.owner = owner;
  }

  public FileBean getLogo() {
    return logo;
  }

  public void setLogo(FileBean logo) {
    this.logo = logo;
  }

  public Integer getLikes() {
    return likes;
  }

  public void setLikes(Integer likes) {
    this.likes = likes;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

}

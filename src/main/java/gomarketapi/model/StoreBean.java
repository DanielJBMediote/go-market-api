package gomarketapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "stores")
public class StoreBean extends Bean {

  @Column(unique = true)
  private String name;
  private String description;

  @Column(nullable = true)
  private String slug;

  private Boolean isActive = true;
  private Integer likes = 0;

  @ManyToOne
  @JoinColumn(name = "owner_id", referencedColumnName = "id")
  private UserBean owner;

  @OneToOne
  @JoinColumn(name = "logo_id", referencedColumnName = "id")
  private FileBean logo;

  public StoreBean() {
  }

  public StoreBean(Long id) {
    super(id);
  }

  public StoreBean(String name, String slug, String description, Boolean isActive, UserBean owner, FileBean logo) {
    this.name = name;
    this.slug = slug;
    this.description = description;
    this.isActive = isActive;
    this.owner = owner;
    this.logo = logo;
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

  public UserBean getOwner() {
    return owner;
  }

  public void setOwner(UserBean owner) {
    this.owner = owner;
  }

  public FileBean getLogo() {
    return logo;
  }

  public void setLogo(FileBean logo) {
    this.logo = logo;
  }

}

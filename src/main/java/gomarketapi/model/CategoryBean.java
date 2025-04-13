package gomarketapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class CategoryBean extends Bean {

  private String name;
  private String subName;

  public CategoryBean() {
  }

  public CategoryBean(Long id) {
    super(id);
  }

  public CategoryBean(String name, String subName) {
    this.name = name;
    this.subName = subName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSubName() {
    return subName;
  }

  public void setSubName(String subName) {
    this.subName = subName;
  }

}

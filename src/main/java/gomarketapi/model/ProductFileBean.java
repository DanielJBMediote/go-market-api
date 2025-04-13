package gomarketapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_files")
public class ProductFileBean extends Bean {

  @ManyToOne
  @JoinColumn(name = "product_id", referencedColumnName = "id")
  @JsonBackReference
  private ProductBean product;

  @OneToOne
  @JoinColumn(name = "file_id", referencedColumnName = "id")
  private FileBean file;

  public ProductFileBean() {
  }

  public ProductFileBean(Long id) {
    super(id);
  }

  public ProductFileBean(ProductBean productBean, FileBean fileBean) {
    this.product = productBean;
    this.file = fileBean;
  }

  public ProductBean getProduct() {
    return product;
  }

  public void setProduct(ProductBean product) {
    this.product = product;
  }

  public FileBean getFile() {
    return file;
  }

  public void setFile(FileBean file) {
    this.file = file;
  }

}

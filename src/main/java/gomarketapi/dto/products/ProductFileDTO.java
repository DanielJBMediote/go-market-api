package gomarketapi.dto.products;

import java.time.LocalDateTime;

import gomarketapi.dto.FileDTO;
import gomarketapi.mapper.FileMapper;
import gomarketapi.model.ProductFileBean;

public class ProductFileDTO {

  private Long id;
  private FileDTO file;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public ProductFileDTO() {

  }

  public ProductFileDTO(ProductFileBean productFileBean) {
    this.id = productFileBean.getId();
    this.file = FileMapper.toDTO(productFileBean.getFile());
    this.createdAt = productFileBean.getCreatedAt();
    this.updatedAt = productFileBean.getUpdatedAt();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public FileDTO getFile() {
    return file;
  }

  public void setFile(FileDTO file) {
    this.file = file;
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

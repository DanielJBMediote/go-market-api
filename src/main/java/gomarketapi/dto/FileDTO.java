package gomarketapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import gomarketapi.model.FileBean;

public class FileDTO {

  private Long id;
  private String name;
  private String uuid;
  private String ext;
  private BigDecimal size;
  private String type;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public FileDTO() {

  }

  public FileDTO(FileBean fileBean) {
    this.id = fileBean.getId();
    this.name = fileBean.getName();
    this.size = fileBean.getSize();
    this.type = fileBean.getType();
    this.ext = fileBean.getExt();
    this.uuid = fileBean.getUuid();
    this.createdAt = fileBean.getCreatedAt();
    this.updatedAt = fileBean.getUpdatedAt();
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

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getExt() {
    return ext;
  }

  public void setExt(String ext) {
    this.ext = ext;
  }

  public BigDecimal getSize() {
    return size;
  }

  public void setSize(BigDecimal size) {
    this.size = size;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

package gomarketapi.dto.cupons;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import gomarketapi.model.CuponBean;

public class CuponReadDTO {

  private Long id;
  private Integer used;
  private Integer maxUsed;
  private String cuponCode;
  private Boolean expired;
  private BigDecimal percentDiscount;
  private LocalDateTime expiredDate;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public CuponReadDTO() {

  }

  public CuponReadDTO(CuponBean cuponBean) {
    this.id = cuponBean.getId();
    this.cuponCode = cuponBean.getCuponCode();
    this.expired = cuponBean.getExpired();
    this.used = cuponBean.getUsed();
    this.maxUsed = cuponBean.getMaxUsed();
    this.percentDiscount = cuponBean.getPercentDiscount();
    this.updatedAt = cuponBean.getUpdatedAt();
    this.createdAt = cuponBean.getCreatedAt();
    this.expiredDate = cuponBean.getExpiredDate();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getUsed() {
    return used;
  }

  public void setUsed(Integer used) {
    this.used = used;
  }

  public Integer getMaxUsed() {
    return maxUsed;
  }

  public void setMaxUsed(Integer maxUsed) {
    this.maxUsed = maxUsed;
  }

  public String getCuponCode() {
    return cuponCode;
  }

  public void setCuponCode(String cuponCode) {
    this.cuponCode = cuponCode;
  }

  public Boolean getExpired() {
    return expired;
  }

  public void setExpired(Boolean expired) {
    this.expired = expired;
  }

  public BigDecimal getPercentDiscount() {
    return percentDiscount;
  }

  public void setPercentDiscount(BigDecimal percentDiscount) {
    this.percentDiscount = percentDiscount;
  }

  public LocalDateTime getExpiredDate() {
    return expiredDate;
  }

  public void setExpiredDate(LocalDateTime expiredDate) {
    this.expiredDate = expiredDate;
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

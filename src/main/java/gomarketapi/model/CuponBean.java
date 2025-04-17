package gomarketapi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "cupons")
public class CuponBean extends Bean {

  private Integer used = 0;
  private Integer maxUsed = 1;

  @Column(unique = true)
  private String cuponCode;

  private BigDecimal percentDiscount;
  private Boolean expired = false;
  private LocalDateTime expiredDate;

  public CuponBean() {
  }

  public CuponBean(Long id) {
    super(id);
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

  public BigDecimal getPercentDiscount() {
    return percentDiscount;
  }

  public void setPercentDiscount(BigDecimal percentDiscount) {
    this.percentDiscount = percentDiscount;
  }

  public Boolean getExpired() {
    return expired;
  }

  public void setExpired(Boolean expired) {
    this.expired = expired;
  }

  public LocalDateTime getExpiredDate() {
    return expiredDate;
  }

  public void setExpiredDate(LocalDateTime expiredDate) {
    this.expiredDate = expiredDate;
  }

}

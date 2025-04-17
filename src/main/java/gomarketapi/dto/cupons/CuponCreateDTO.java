package gomarketapi.dto.cupons;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CuponCreateDTO {

  private Integer maxUsed;
  private String cuponCode;
  private BigDecimal percentDiscount;
  private LocalDateTime expiredDate;

  public Integer getMaxUsed() {
    return maxUsed;
  }

  public String getCuponCode() {
    return cuponCode;
  }

  public BigDecimal getPercentDiscount() {
    return percentDiscount;
  }

  public LocalDateTime getExpiredDate() {
    return expiredDate;
  }

}

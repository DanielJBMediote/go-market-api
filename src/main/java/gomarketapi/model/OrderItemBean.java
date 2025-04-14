package gomarketapi.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItemBean extends Bean {

  @ManyToOne
  @JsonBackReference
  private OrderBean order;

  @ManyToOne
  @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
  private ProductBean product;

  private Integer quantity;
  private BigDecimal unitPrice;

  public OrderItemBean() {
  }

  public OrderItemBean(Long id) {
    super(id);
  }

  public OrderBean getOrder() {
    return order;
  }

  public void setOrder(OrderBean order) {
    this.order = order;
  }

  public ProductBean getProduct() {
    return product;
  }

  public void setProduct(ProductBean product) {
    this.product = product;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

}
package gomarketapi.dto;

import java.math.BigDecimal;

import gomarketapi.dto.products.ProductReadDTO;
import gomarketapi.model.OrderItemBean;

public class OrderItemDTO {

  private Long id;
  private ProductReadDTO product;
  private OrderDTO order;
  private Integer quantity;
  private BigDecimal unitPrice;

  public OrderItemDTO() {

  }

  public OrderItemDTO(OrderItemBean orderItemBean) {
    this.id = orderItemBean.getId();

    ProductReadDTO productDTO = new ProductReadDTO();
    productDTO.setId(orderItemBean.getProduct().getId());
    productDTO.setName(orderItemBean.getProduct().getName());
    productDTO.setPrice(orderItemBean.getProduct().getPrice());

    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setId(orderItemBean.getProduct().getStore().getId());
    productDTO.setStore(storeDTO);
    this.product = productDTO;

    this.quantity = orderItemBean.getQuantity();
    this.unitPrice = orderItemBean.getUnitPrice();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProductReadDTO getProductId() {
    return product;
  }

  public void setProductId(ProductReadDTO product) {
    this.product = product;
  }

  public OrderDTO getOrder() {
    return order;
  }

  public void setOrder(OrderDTO order) {
    this.order = order;
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

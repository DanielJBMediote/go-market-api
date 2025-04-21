package gomarketapi.dto.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import gomarketapi.dto.UserDTO;
import gomarketapi.enums.OrderStatus;
import gomarketapi.enums.PaymentMethod;
import gomarketapi.mapper.UserMapper;
import gomarketapi.model.OrderBean;

public class OrderReadDTO {

  private Long id;
  private UserDTO client;

  private List<OrderItemDTO> items;
  private PaymentMethod paymentMethod;
  private BigDecimal totalAmount;
  private BigDecimal taxes;
  private BigDecimal discount;
  private String shippingAddress;
  private String uuid;

  private OrderStatus status;
  private LocalDateTime paymentDate;
  private LocalDateTime deliveryDate;

  public OrderReadDTO() {

  }

  public OrderReadDTO(OrderBean orderBean) {
    this.id = orderBean.getId();
    this.client = UserMapper.toDTO(orderBean.getClient());
    this.status = orderBean.getStatus();
    this.paymentDate = orderBean.getPaymentDate();
    this.paymentMethod = orderBean.getPaymentMethod();
    this.deliveryDate = orderBean.getDeliveryDate();
    this.shippingAddress = orderBean.getShippingAddress();
    this.taxes = orderBean.getTaxes();
    this.discount = orderBean.getDiscount();
    this.totalAmount = orderBean.getTotalAmount();
    this.items = orderBean.getItems().stream().map(item -> new OrderItemDTO(item)).toList();
    this.uuid = orderBean.getUuid();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserDTO getClient() {
    return client;
  }

  public void setClient(UserDTO client) {
    this.client = client;
  }

  public List<OrderItemDTO> getItems() {
    return items;
  }

  public void setItems(List<OrderItemDTO> items) {
    this.items = items;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public BigDecimal getTaxes() {
    return taxes;
  }

  public void setTaxes(BigDecimal taxes) {
    this.taxes = taxes;
  }

  public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(BigDecimal discont) {
    this.discount = discont;
  }

  public String getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public LocalDateTime getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(LocalDateTime paymentDate) {
    this.paymentDate = paymentDate;
  }

  public LocalDateTime getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(LocalDateTime deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

}
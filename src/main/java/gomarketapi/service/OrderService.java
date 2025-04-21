package gomarketapi.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import gomarketapi.contexts.UserContext;
import gomarketapi.dto.cupons.CuponReadDTO;
import gomarketapi.dto.orders.OrderCreateDTO;
import gomarketapi.dto.orders.OrderItemDTO;
import gomarketapi.dto.orders.OrderReadDTO;
import gomarketapi.enums.OrderStatus;
import gomarketapi.exceptions.NotFoundException;
import gomarketapi.helpers.Where;
import gomarketapi.helpers.WhereBuilder;
import gomarketapi.model.CuponBean;
import gomarketapi.model.OrderBean;
import gomarketapi.model.OrderItemBean;
import gomarketapi.model.ProductBean;
import gomarketapi.model.UserBean;
import gomarketapi.repository.OrderItemRepository;
import gomarketapi.repository.OrderRepository;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final UserContext userContext;
  private final CuponService cuponService;

  public OrderService(OrderRepository orderRepository, UserContext userContext,
      OrderItemRepository orderItemRepository, CuponService cuponService) {
    this.orderRepository = orderRepository;
    this.orderItemRepository = orderItemRepository;
    this.userContext = userContext;
    this.cuponService = cuponService;
  }

  public OrderReadDTO create(OrderCreateDTO orderBody) {

    OrderBean orderBean = new OrderBean();

    orderBean.setUuid(UUID.randomUUID().toString());
    orderBean.setStatus(OrderStatus.PENDING);
    orderBean.setTaxes(orderBody.getTaxes());
    orderBean.setClient(new UserBean(userContext.getUserId()));
    orderBean.setDeliveryDate(null);
    orderBean.setPaymentMethod(orderBody.getPaymentMethod());
    orderBean.setShippingAddress(orderBody.getShippingAddress());

    List<OrderItemBean> items = new ArrayList<>();
    BigDecimal totalAmount = BigDecimal.ZERO;

    for (OrderItemDTO itemDTO : orderBody.getItems()) {
      OrderItemBean itemBean = new OrderItemBean();

      BigDecimal productPrice = itemDTO.getProduct().getPrice();
      Integer quantity = itemDTO.getQuantity();
      BigDecimal totalPerProduct = productPrice.multiply(BigDecimal.valueOf(quantity));

      Long productId = itemDTO.getProduct().getId();
      ProductBean productBean = new ProductBean(productId);
      itemBean.setProduct(productBean);
      itemBean.setQuantity(quantity);
      itemBean.setUnitPrice(productPrice);

      totalAmount = totalAmount.add(totalPerProduct);
      items.add(itemBean);
    }

    // Agora sim setamos o total e salvamos o pedido
    BigDecimal discount = this.applyCuponDiscount(totalAmount, orderBean, orderBody);
    orderBean.setDiscount(discount);

    BigDecimal totalWithDiscount = totalAmount.subtract(discount);
    orderBean.setTotalAmount(totalWithDiscount);

    OrderBean createdOrder = orderRepository.save(orderBean);

    // Vinculamos os itens ao pedido e persistimos
    for (OrderItemBean item : items) {
      item.setOrder(orderBean);
    }
    orderItemRepository.saveAll(items);
    orderBean.setItems(items);

    OrderReadDTO orderDTO = new OrderReadDTO(createdOrder);
    return orderDTO;
  }

  private BigDecimal applyCuponDiscount(BigDecimal subTotal, OrderBean orderBean, OrderCreateDTO orderBody) {
    if (orderBody.getCupon() == null || orderBody.getCupon().getId() == null) {
      return BigDecimal.ZERO;
    }

    Long cuponId = orderBody.getCupon().getId();
    orderBean.setCupon(new CuponBean(cuponId));

    try {
      CuponReadDTO usedCupon = this.cuponService.getById(cuponId);
      CuponReadDTO cupon = this.cuponService.useCuponByCode(usedCupon.getCuponCode());

      BigDecimal total = subTotal.add(orderBean.getTaxes());
      BigDecimal discountPercent = cupon.getPercentDiscount();

      // Calcula o valor do desconto com precisão decimal
      BigDecimal discountAmount = total.multiply(discountPercent).divide(BigDecimal.valueOf(100));

      return discountAmount;
    } catch (NotFoundException e) {
      throw new NotFoundException("Can't apply discount, cupon not found!");
    }
  }

  public List<OrderReadDTO> getAll(String where) {

    Long userId = userContext.getUserId();

    WhereBuilder<OrderBean> wb = new WhereBuilder<>(OrderBean.class);
    wb.parseWhereEncodedURIParams(where);
    wb.addWhere(new Where("items.product.store.owner.id", userId, Where.Operator.EQUALS));
    wb.addWhere(new Where("items.product.store.isActive", true, Where.Operator.EQUALS));
    wb.addWhere(new Where("items.product.isActive", true, Where.Operator.EQUALS));
    Specification<OrderBean> spec = wb.build();

    List<OrderBean> list = this.orderRepository.findAll(spec);

    list.sort((o1, o2) -> {
      List<OrderStatus> priority = List.of(
          OrderStatus.PENDING,
          OrderStatus.PROCESSING,
          OrderStatus.SHIPPED,
          OrderStatus.DELIVERED,
          OrderStatus.CANCELLED,
          OrderStatus.REFUNDED,
          OrderStatus.FAILED,
          OrderStatus.COMPLETED);
      return Integer.compare(priority.indexOf(o1.getStatus()), priority.indexOf(o2.getStatus()));
    });

    List<OrderReadDTO> orderReadList = list.stream().map(item -> new OrderReadDTO(item)).toList();
    return orderReadList;
  }

  public OrderReadDTO getById(Long id) {
    OrderBean orderBean = this.orderRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Order not found with ID: " + id));
    OrderReadDTO orderReadDTO = new OrderReadDTO(orderBean);
    return orderReadDTO;
  }

  public void updateStatus(Long id) {
    OrderBean orderBean = this.orderRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Order not found with ID: " + id));

    switch (orderBean.getStatus()) {
      case OrderStatus.PENDING:
        orderBean.setStatus(OrderStatus.PROCESSING);
        break;
      case OrderStatus.PROCESSING:
        orderBean.setStatus(OrderStatus.SHIPPED);
        break;
      case OrderStatus.SHIPPED:
        orderBean.setStatus(OrderStatus.DELIVERED);
        break;
      case OrderStatus.DELIVERED:
        orderBean.setStatus(OrderStatus.COMPLETED);
        break;
      default:
        break;
    }

    this.orderRepository.save(orderBean);
  }

}

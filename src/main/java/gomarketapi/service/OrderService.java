package gomarketapi.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import gomarketapi.contexts.UserContext;
import gomarketapi.dto.OrderDTO;
import gomarketapi.enums.OrderStatus;
import gomarketapi.exceptions.NotFoundException;
import gomarketapi.helpers.Where;
import gomarketapi.helpers.WhereBuilder;
import gomarketapi.model.OrderBean;
import gomarketapi.model.OrderItemBean;
import gomarketapi.repository.OrderRepository;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserContext userContext;

  public OrderService(OrderRepository orderRepository, UserContext userContext) {
    this.orderRepository = orderRepository;
    this.userContext = userContext;
  }

  public OrderDTO create(OrderDTO orderDTO) {
    OrderBean orderBean = new OrderBean();

    orderBean.setStatus(OrderStatus.PENDING);

    orderBean.setTaxes(orderDTO.getTaxes());
    orderBean.setDiscont(orderDTO.getDiscont());
    orderBean.setTotalAmount(orderDTO.getTotalAmount());
    orderBean.setUUID(UUID.randomUUID().toString());

    orderBean.setDeliveredDate(orderDTO.getDeliveredDate());
    orderBean.setPaymentMethod(orderDTO.getPaymentMethod());
    orderBean.setShippingAddress(orderDTO.getShippingAddress());

    orderBean.setItems(orderDTO.getItems()
        .stream()
        .map(orderItemDTO -> new OrderItemBean(orderItemDTO.getId()))
        .toList());

    orderBean = this.orderRepository.save(orderBean);
    return new OrderDTO(orderBean);
  }

  public List<OrderDTO> getAll(String where) {
    Long userId = userContext.getUserId();

    WhereBuilder<OrderBean> wb = new WhereBuilder<>(OrderBean.class);
    wb.parseWhereEncodedURIParams(where);
    wb.addWhere(new Where("items.product.store.owner.id", userId, Where.Operator.EQUALS));
    wb.addWhere(new Where("items.product.store.isActive", true, Where.Operator.EQUALS));
    wb.addWhere(new Where("items.product.isActive", true, Where.Operator.EQUALS));
    Specification<OrderBean> spec = wb.build();

    List<OrderBean> list = this.orderRepository.findAll(spec);

    return list.stream().map(item -> new OrderDTO(item)).toList();
  }

  public OrderDTO getById(Long id) {
    OrderBean orderBean = this.orderRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Order not found with ID: " + id));

    return new OrderDTO(orderBean);
  }

}

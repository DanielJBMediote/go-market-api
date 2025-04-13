package gomarketapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gomarketapi.dto.OrderDTO;
import gomarketapi.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public OrderDTO create(@RequestBody OrderDTO orderDTO) {
    return this.orderService.create(orderDTO);
  }

  @GetMapping
  public List<OrderDTO> getAll(@RequestParam(required = false) String where) {
    return this.orderService.getAll(where);
  }

  @GetMapping("/{id}")
  public OrderDTO getById(@PathVariable Long id) {
    return this.orderService.getById(id);
  }

}

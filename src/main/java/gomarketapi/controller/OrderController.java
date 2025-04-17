package gomarketapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gomarketapi.dto.orders.OrderCreateDTO;
import gomarketapi.dto.orders.OrderReadDTO;
import gomarketapi.exceptions.NotFoundException;
import gomarketapi.helpers.ResponseBuilder;
import gomarketapi.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody OrderCreateDTO bodyDTO) {
    try {
      OrderReadDTO orderDTO = this.orderService.create(bodyDTO);
      return ResponseBuilder.created("Order has been created sucessfully!", orderDTO);
    } catch (Exception e) {
      return ResponseBuilder.badRequest("Failed to create Order. Cause " + e.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<?> getAll(@RequestParam(required = false) String where) {
    List<OrderReadDTO> list = this.orderService.getAll(where);
    return ResponseBuilder.success(list);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) {
    try {
      OrderReadDTO orderReadDTO = this.orderService.getById(id);
      return ResponseBuilder.success(orderReadDTO);
    } catch (NotFoundException e) {
      return ResponseBuilder.notFound(e.getMessage());
    }
  }

  @GetMapping("/{id}/update-status")
  public ResponseEntity<?> updateStatus(@PathVariable Long id) {
    try {
      this.orderService.updateStatus(id);
      return ResponseBuilder.success("Order has been updated!");
    } catch (Exception e) {
      return ResponseBuilder.badRequest("Failed to update status. Cause: " + e.getMessage());
    }
  }

}

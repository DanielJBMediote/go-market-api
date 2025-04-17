package gomarketapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gomarketapi.dto.products.comments.ProductCommentReadDTO;
import gomarketapi.exceptions.NotFoundException;
import gomarketapi.helpers.ResponseBuilder;
import gomarketapi.service.ProductCommentService;

@RestController
@RequestMapping("/api/products/{id}")
public class ProductCommentController {

  private final ProductCommentService productCommentService;

  ProductCommentController(ProductCommentService productCommentService) {
    this.productCommentService = productCommentService;
  }

  @PostMapping("/comment")
  public ResponseEntity<?> createComment(
      @PathVariable Long id,
      @RequestBody String comment) {
    try {
      ProductCommentReadDTO procutCommentReadDTO = this.productCommentService.create(id, comment);
      return ResponseBuilder.created("Comment has been created.", procutCommentReadDTO);
    } catch (NotFoundException e) {
      return ResponseBuilder.notFound(e.getMessage());
    } catch (Exception e) {
      return ResponseBuilder.badRequest(e.getMessage());
    }
  }

  @PutMapping("/comment/{pcId}")
  public ResponseEntity<?> updateComment(
      @PathVariable Long id,
      @PathVariable Long pcId,
      @RequestBody String comment) {

    try {
      ProductCommentReadDTO productCommentDTO = this.productCommentService.update(id, pcId, comment);
      return ResponseBuilder.success("Comment has been updated sucessfully!", productCommentDTO);
    } catch (NotFoundException e) {
      return ResponseBuilder.notFound(e.getMessage());
    }
  }

  @GetMapping("/comment")
  public ResponseEntity<?> getComments(@PathVariable Long id) {
    List<ProductCommentReadDTO> list = this.productCommentService.getAllComments(id);
    return ResponseBuilder.success(list);
  }

}

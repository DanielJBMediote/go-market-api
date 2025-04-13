package gomarketapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gomarketapi.dto.products.comments.ProductCommentReadDTO;
import gomarketapi.service.ProductCommentService;

@RestController
@RequestMapping("/api/products/{id}")
public class ProductCommentController {

  private final ProductCommentService productCommentService;

  ProductCommentController(ProductCommentService productCommentService) {
    this.productCommentService = productCommentService;
  }

  @PostMapping("/comment")
  public ProductCommentReadDTO createComment(
      @PathVariable Long id,
      @RequestBody String comment) {
    return this.productCommentService.create(id, comment);
  }

  @PutMapping("/comment/{pcId}")
  public ProductCommentReadDTO updateComment(
      @PathVariable Long id,
      @PathVariable Long pcId,
      @RequestBody String comment) {

    return this.productCommentService.update(id, pcId, comment);
  }

  @GetMapping("/comment")
  public List<ProductCommentReadDTO> getComments(@PathVariable Long id) {
    return this.productCommentService.getAllComments(id);
  }

}

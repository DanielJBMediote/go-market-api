package gomarketapi.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gomarketapi.dto.products.ProductCreateDTO;
import gomarketapi.dto.products.ProductReadDTO;
import gomarketapi.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> create(
      @RequestPart(value = "data") ProductCreateDTO body,
      @RequestPart(value = "files", required = false) MultipartFile[] files) throws Exception {

    return this.productService.create(body, files);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) throws Exception {
    return this.productService.getById(id);
  }

  @GetMapping
  public ResponseEntity<?> getAll(@RequestParam(required = false) String where) throws Exception {
    return this.productService.getAll(where);
  }

  @GetMapping("/featured")
  public ResponseEntity<?> getAllFeatued(@RequestParam(required = false) String where) throws Exception {
    return this.productService.getAllFeatured(where);
  }

  @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> update(
      @PathVariable Long id,
      @RequestPart("data") ProductReadDTO body,
      @RequestPart(value = "files", required = false) MultipartFile[] files) throws Exception {

    return this.productService.update(id, body, files);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
    return this.productService.delete(id);
  }

  @PutMapping("/mark-as-featured")
  public ResponseEntity<?> updateProductsAsFeatured(@RequestBody List<Long> prodIds) {
    return this.productService.updateProductsAsFeatured(prodIds);
  }

}

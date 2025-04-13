package gomarketapi.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  public ProductReadDTO create(
      @RequestPart(value = "data") ProductCreateDTO body,
      @RequestPart(value = "files", required = false) MultipartFile[] files) throws Exception {

    ProductReadDTO prodDTO = this.productService.create(body);
    this.productService.updateImages(prodDTO.getId(), prodDTO, files);

    return prodDTO;
  }

  @GetMapping("/{id}")
  public ProductReadDTO getById(@PathVariable Long id) throws Exception {
    return this.productService.getById(id);
  }

  @GetMapping
  public List<ProductReadDTO> getAll(@RequestParam(required = false) String where) throws Exception {
    return this.productService.getAll(where);
  }

  @GetMapping("/featured")
  public List<ProductReadDTO> getAllFeatued(@RequestParam(required = false) String where) throws Exception {
    return this.productService.getAllFeatured(where);
  }

  @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ProductReadDTO update(
      @PathVariable Long id,
      @RequestPart("data") ProductReadDTO body,
      @RequestPart(value = "files", required = false) MultipartFile[] files) throws Exception {

    this.productService.update(id, body);
    this.productService.updateImages(id, body, files);

    return this.productService.getById(id);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws Exception {
    this.productService.delete(id);
  }

}

package gomarketapi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import gomarketapi.dto.products.ProductCreateDTO;
import gomarketapi.dto.products.ProductReadDTO;
import gomarketapi.exceptions.NotFoundException;
import gomarketapi.helpers.ResponseBuilder;
import gomarketapi.helpers.Where;
import gomarketapi.helpers.WhereBuilder;
import gomarketapi.mapper.ProductMapper;
import gomarketapi.model.CategoryBean;
import gomarketapi.model.FileBean;
import gomarketapi.model.ProductBean;
import gomarketapi.model.ProductFileBean;
import gomarketapi.model.StoreBean;
import gomarketapi.repository.CategoryRepository;
import gomarketapi.repository.FileRepository;
import gomarketapi.repository.ProductFileRepository;
import gomarketapi.repository.ProductRepository;
import gomarketapi.repository.StoreRepository;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final StoreRepository storeRepository;
  private final FileRepository fileRepository;
  private final FileService fileService;
  private final ProductFileRepository productFileRepository;

  public ProductService(
      ProductRepository productRepository,
      CategoryRepository categoryRepository,
      StoreRepository storeRepository,
      FileRepository fileRepository,
      FileService fileService,
      ProductFileRepository productFileRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
    this.storeRepository = storeRepository;
    this.fileRepository = fileRepository;
    this.fileService = fileService;
    this.productFileRepository = productFileRepository;
  }

  public ResponseEntity<?> create(ProductCreateDTO productBody, MultipartFile[] files) {

    try {

      ProductBean productBean = new ProductBean();

      productBean.setName(productBody.getName());
      productBean.setDescription(productBody.getDescription());
      productBean.setIsActive(true);
      productBean.setIsFeatured(false);

      CategoryBean categoryBean = this.categoryRepository.findById(productBody.getCategory().getId())
          .orElseThrow(
              () -> new NotFoundException("Category not found with id: " + productBody.getCategory().getId()));
      StoreBean storeBean = this.storeRepository.findById(productBody.getStore().getId())
          .orElseThrow(() -> new NotFoundException("Store not found with id: " + productBody.getStore().getId()));

      productBean.setCategory(categoryBean);
      productBean.setStore(storeBean);

      productBean.setPrice(productBody.getPrice());
      productBean.setLowestPrice(productBody.getPrice());
      productBean.setHighestPrice(productBody.getPrice());

      ProductBean createdProduct = this.productRepository.save(productBean);
      ProductReadDTO productReadDTO = ProductMapper.toDTO(createdProduct);

      this.updateImages(productReadDTO.getId(), productReadDTO, files);
      return ResponseBuilder.created("Product created Successfully!", productReadDTO);
    } catch (Exception e) {
      return ResponseBuilder.badRequest("Failed to create Product! Cause: " + e.getMessage());
    }
  }

  public ResponseEntity<?> getById(Long id) {
    try {
      ProductBean prodBean = this.productRepository.findById(id)
          .orElseThrow(() -> new NotFoundException("Product not found with id: " + id + "."));
      ProductReadDTO productReadtDTO = ProductMapper.toDTO(prodBean);
      return ResponseBuilder.success(productReadtDTO);
    } catch (NotFoundException e) {
      return ResponseBuilder.notFound(e.getMessage());
    }

  }

  public ResponseEntity<?> getAll(String encodedURI) {

    WhereBuilder<ProductBean> wb = new WhereBuilder<>(ProductBean.class);
    // wb.addWhere(new Where("isActive", true, Where.Operator.EQUALS));

    Specification<ProductBean> spec = wb.parseWhereEncodedURIParams(encodedURI).build();
    List<ProductBean> productList = this.productRepository.findAll(spec);

    List<ProductReadDTO> productReadDTOList = productList.stream().map(ProductMapper::toDTO).toList();
    return ResponseBuilder.success(productReadDTOList);
  }

  public ResponseEntity<?> update(Long id, ProductReadDTO productBody, MultipartFile[] files) {
    try {

      ProductBean prodBean = this.productRepository.findById(id)
          .orElseThrow(() -> new NotFoundException("Product not found with id: " + id + "."));

      prodBean.setName(productBody.getName());
      prodBean.setDescription(productBody.getDescription());
      prodBean.setIsActive(productBody.getIsActive());
      prodBean.setIsFeatured(productBody.getIsFeatured());
      prodBean.getStore().setId(productBody.getStore().getId());

      // Carrega a categoria existente do banco (ou verifica se existe)
      CategoryBean categoryBean = this.categoryRepository.findById(productBody.getCategory().getId())
          .orElseThrow(() -> new NotFoundException("Category not found with id: " + productBody.getCategory().getId()));

      prodBean.setCategory(categoryBean);
      prodBean.setPrice(productBody.getPrice());

      // Update LowestPrice only if the current Price is less than the old LowestPrice
      if (prodBean.getPrice().compareTo(prodBean.getLowestPrice()) == -1) {
        prodBean.setLowestPrice(prodBean.getPrice());
      }
      // Update HighestPrice only if the current Price is greater than the old
      // HighestPrice
      if (prodBean.getPrice().compareTo(prodBean.getHighestPrice()) == 1) {
        prodBean.setHighestPrice(prodBean.getPrice());
      }

      ProductBean updatedProductBean = this.productRepository.save(prodBean);
      ProductReadDTO productReadtDTO = ProductMapper.toDTO(updatedProductBean);
      this.updateImages(id, productBody, files);
      return ResponseBuilder.success("Product has been updated sucessfully!", productReadtDTO);
    } catch (Exception e) {
      return ResponseBuilder.success("Faile to update Product! Cause " + e.getMessage());
    }
  }

  public ResponseEntity<?> delete(Long id) {

    try {
      ProductBean productBean = productRepository.findById(id)
          .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));

      this.productRepository.delete(productBean);

      return ResponseBuilder.success("Product has been deleted sucessfully!");
    } catch (Exception e) {
      return ResponseBuilder.badRequest("Failed to delete product! Cause " + e.getMessage());
    }
  }

  @Transactional
  private void updateImages(Long id, ProductReadDTO previousProduct, MultipartFile[] files) throws Exception {

    ProductBean productBean = productRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));

    WhereBuilder<ProductFileBean> wb = new WhereBuilder<>(ProductFileBean.class);
    wb.addWhere(new Where("product.id", productBean.getId(), Where.Operator.EQUALS));

    Specification<ProductFileBean> spec = wb.build();
    List<ProductFileBean> existingProductFiles = this.productFileRepository.findAll(spec);

    if (productBean.getImages() == null) {
      productBean.setImages(new ArrayList<>());
    }

    List<Long> remainingImageIds = previousProduct.getImages().stream().map(pf -> pf.getId()).toList();

    // 4. Filtra os arquivos existentes para deletar apenas os que não estão na
    // lista
    // nova
    List<ProductFileBean> productFilesToDelete = existingProductFiles.stream()
        .filter(pf -> !remainingImageIds.contains(pf.getId()))
        .toList();

    // 5. Deleta os arquivos obsoletos (via repositório para evitar problemas com
    // JPA)
    List<FileBean> filesToDelete = productFilesToDelete.stream().map(pf -> pf.getFile()).toList();

    this.fileRepository.deleteAll(filesToDelete);
    this.productFileRepository.deleteAll(productFilesToDelete);

    productBean.getImages().removeAll(productFilesToDelete);
    // 6. Adiciona os novos arquivos (apenas os que não existiam ou foram
    // modificados)
    if (files != null && files.length > 0) {
      Arrays.stream(files)
          .filter(file -> {
            // Verifica se o arquivo já existe (com mesmo nome e tamanho)
            return existingProductFiles.stream()
                .noneMatch(existingFile -> existingFile.getFile().getName().equals(file.getOriginalFilename()) &&
                    existingFile.getFile().getSize().longValue() == file.getSize());
          })
          .forEach(file -> {
            try {
              FileBean newFileBean = fileService.createFileBean(file);
              FileBean savedFile = fileRepository.save(newFileBean);

              ProductFileBean productFile = new ProductFileBean();
              productFile.setFile(savedFile);
              productFile.setProduct(productBean);

              productBean.getImages().add(productFile);
            } catch (IOException e) {
              throw new RuntimeException("Failed to process file: " + file.getOriginalFilename(), e);
            }
          });
    }

    // 7. Atualiza o produto (sem necessidade de clear() manual)
    productRepository.save(productBean);
  }

  public ResponseEntity<?> getAllFeatured(String encodedURI) {
    WhereBuilder<ProductBean> wb = new WhereBuilder<>(ProductBean.class);
    wb.parseWhereEncodedURIParams(encodedURI);
    wb.addWhere(new Where("isActive", true, Where.Operator.EQUALS));

    Specification<ProductBean> spec = wb.build();
    List<ProductBean> productList = this.productRepository.findAll(spec);

    productList.sort((pb1, pb2) -> {
      int f1 = pb1.getIsFeatured() ? 1 : 0;
      int f2 = pb2.getIsFeatured() ? 1 : 0;
      return f2 - f1;
    });

    List<ProductReadDTO> productReadDTOList = productList.stream().map(ProductMapper::toDTO).toList();
    return ResponseBuilder.success(productReadDTOList);
  }

  public ResponseEntity<?> updateProductsAsFeatured(List<Long> prodIds) {
    try {

      WhereBuilder<ProductBean> wb = new WhereBuilder<>(ProductBean.class);
      wb.addWhere(new Where("id", prodIds, Where.Operator.IN));
      Specification<ProductBean> spec = wb.build();

      List<ProductBean> prodList = this.productRepository.findAll(spec);

      for (ProductBean productBean : prodList) {
        if (productBean.getIsFeatured()) {
          continue;
        }
        productBean.setIsFeatured(true);
      }
      this.productRepository.saveAll(prodList);
      return ResponseBuilder.success(prodList.size() + " is marked as Featured!");
    } catch (Exception e) {
      return ResponseBuilder.badRequest("Failed to update products to 'Featured'. Cause " + e.getMessage());
    }
  }

}

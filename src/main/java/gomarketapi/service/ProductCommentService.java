package gomarketapi.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import gomarketapi.dto.products.comments.ProductCommentReadDTO;
import gomarketapi.exceptions.NotFoundException;
import gomarketapi.helpers.Where;
import gomarketapi.helpers.WhereBuilder;
import gomarketapi.model.CommentBean;
import gomarketapi.model.ProductBean;
import gomarketapi.model.ProductCommentBean;
import gomarketapi.repository.ProductCommentRepository;
import gomarketapi.repository.ProductRepository;

@Service
public class ProductCommentService {

  private final ProductRepository productRepository;
  private final ProductCommentRepository productCommentRepository;
  private final CommentService commentService;

  public ProductCommentService(ProductRepository productRepository,
      ProductCommentRepository productCommentRepository, CommentService commentService) {
    this.productCommentRepository = productCommentRepository;
    this.productRepository = productRepository;
    this.commentService = commentService;
  }

  public ProductCommentReadDTO create(Long id, String comment) {
    ProductBean pBean = this.productRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Product not foud with id " + id));

    CommentBean comBean = this.commentService.create(comment);
    ProductCommentBean pcBean = new ProductCommentBean();
    pcBean.setProduct(pBean);
    pcBean.setComment(comBean);

    ProductCommentBean createPcbean = this.productCommentRepository.save(pcBean);
    return new ProductCommentReadDTO(createPcbean);
  }

  public ProductCommentReadDTO update(Long id, Long prodComId, String comment) {
    ProductCommentBean pcBean = this.productCommentRepository.findById(prodComId)
        .orElseThrow(() -> new NotFoundException("Product Comment not foud with id " + id));

    Long comId = pcBean.getComment().getId();
    CommentBean cBean = this.commentService.update(comId, comment);
    pcBean.setComment(cBean);

    return new ProductCommentReadDTO(pcBean);
  }

  public List<ProductCommentReadDTO> getAllComments(Long id) {

    WhereBuilder<ProductCommentBean> wb = new WhereBuilder<>(ProductCommentBean.class);
    wb.addWhere(new Where("product.id", id, Where.Operator.EQUALS));
    Specification<ProductCommentBean> spec = wb.build();
    List<ProductCommentBean> pcBeanList = this.productCommentRepository.findAll(spec);

    pcBeanList.sort((item1, item2) -> item2.getCreatedAt().compareTo(item1.getCreatedAt()));
    // pcBeanList.sort((item1, item2) ->
    // item2.getComment().getIsPinned().compareTo(item1.getComment().getIsPinned()));

    return pcBeanList.stream().map(item -> new ProductCommentReadDTO(item)).toList();
  }

}

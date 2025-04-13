package gomarketapi.dto.products.comments;

import gomarketapi.dto.CommentDTO;
import gomarketapi.dto.products.ProductReadDTO;
import gomarketapi.model.ProductCommentBean;

public class ProductCommentReadDTO {

  private Long id;
  private ProductReadDTO product;
  private CommentDTO comment;

  public ProductCommentReadDTO() {
  }

  public ProductCommentReadDTO(ProductCommentBean pcBean) {
    this.id = pcBean.getId();
    this.comment = new CommentDTO(pcBean.getComment());
    this.product = new ProductReadDTO(pcBean.getProduct().getId());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProductReadDTO getProduct() {
    return product;
  }

  public void setProduct(ProductReadDTO product) {
    this.product = product;
  }

  public CommentDTO getComment() {
    return comment;
  }

  public void setComment(CommentDTO comment) {
    this.comment = comment;
  }

}

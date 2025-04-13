package gomarketapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_comments")
public class ProductCommentBean extends Bean {

  @ManyToOne
  @JoinColumn(name = "product_id", referencedColumnName = "id")
  @JsonBackReference
  private ProductBean product;

  @ManyToOne
  @JoinColumn(name = "comment_id", referencedColumnName = "id")
  private CommentBean comment;

  public ProductBean getProduct() {
    return product;
  }

  public void setProduct(ProductBean product) {
    this.product = product;
  }

  public CommentBean getComment() {
    return comment;
  }

  public void setComment(CommentBean comment) {
    this.comment = comment;
  }

}

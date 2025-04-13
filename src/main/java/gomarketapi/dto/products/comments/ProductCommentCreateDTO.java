package gomarketapi.dto.products.comments;

public class ProductCommentCreateDTO {

  private Long productId;
  private String comment;

  public Long getProductId() {
    return productId;
  }

  public String getComment() {
    return comment;
  }

}

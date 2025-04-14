package gomarketapi.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import gomarketapi.dto.products.ProductReadDTO;
import gomarketapi.dto.products.comments.ProductCommentReadDTO;
import gomarketapi.model.ProductBean;
import gomarketapi.model.ProductCommentBean;
import gomarketapi.model.ProductFileBean;
import gomarketapi.model.StoreBean;

@Component
public class ProductMapper {

  public static ProductBean toEntityBean(ProductReadDTO prodDTO) {
    ProductBean productBean = new ProductBean();

    productBean.setId(prodDTO.getId());
    productBean.setName(prodDTO.getName());
    productBean.setDescription(prodDTO.getDescription());
    productBean.setPrice(prodDTO.getPrice());
    productBean.setCategory(prodDTO.getCategory());
    productBean.setStore(new StoreBean(prodDTO.getStore().getId()));
    productBean.setHighestPrice(prodDTO.getHighestPrice());
    productBean.setLowestPrice(prodDTO.getLowestPrice());
    productBean.setImages(prodDTO.getImages());
    productBean.setIsActive(prodDTO.getIsActive());
    productBean.setIsFeatured(prodDTO.getIsFeatured());
    // productBean.setLikes(prodDTO.getLikes());
    // productBean.setTimesOrdered(prodDTO.getTimesOrdered());

    return productBean;
  }

  public static ProductReadDTO toDTO(ProductBean productBean) {
    ProductReadDTO productDTO = new ProductReadDTO();

    Long productId = productBean.getId();

    productDTO.setId(productId);
    productDTO.setName(productBean.getName());
    productDTO.setDescription(productBean.getDescription());
    productDTO.setIsActive(productBean.getIsActive());
    productDTO.setIsFeatured(productBean.getIsFeatured());
    productDTO.setPrice(productBean.getPrice());
    productDTO.setLowestPrice(productBean.getLowestPrice());
    productDTO.setHighestPrice(productBean.getHighestPrice());
    productDTO.setLikes(productBean.getLikes());
    productDTO.setTimesOrdered(productBean.getTimesOrdered());
    productDTO.setCategory(productBean.getCategory());
    productDTO.setStore(StoreMapper.toDTO(productBean.getStore()));
    productDTO.setImages(resolveImages(productBean.getImages()));
    productDTO.setComments(mapCommentsToDTO(productBean.getComments()));

    return productDTO;
  }

  private static List<ProductFileBean> resolveImages(List<ProductFileBean> images) {
    if (images == null) {
      images = new ArrayList<>();
    } else {
      images.forEach(image -> {
        image.getFile().setData(null);
        image.getFile().setBase64Data(null);
        image.getFile().setCreatedAt(null);
        image.getFile().setUpdatedAt(null);
        image.setCreatedAt(null);
        image.setUpdatedAt(null);
      });
    }
    return images;
  }

  public static List<ProductCommentReadDTO> mapCommentsToDTO(List<ProductCommentBean> comments) {
    if (comments == null) {
      return new ArrayList<>();
    } else {
      return comments.stream().map(item -> new ProductCommentReadDTO(item)).toList();
    }
  }
}

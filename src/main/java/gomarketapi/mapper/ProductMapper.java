package gomarketapi.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import gomarketapi.dto.products.ProductFileDTO;
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
    // productBean.setImages(prodDTO.getImages());
    productBean.setIsActive(prodDTO.getIsActive());
    productBean.setIsFeatured(prodDTO.getIsFeatured());

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
    productDTO.setImages(mapImagesToDTO(productBean.getImages()));
    productDTO.setComments(mapCommentsToDTO(productBean.getComments()));

    return productDTO;
  }

  public static List<ProductFileDTO> mapImagesToDTO(List<ProductFileBean> images) {
    if (images == null) {
      return new ArrayList<>();
    } else {
      return images.stream().map(ProductMapper::convertProductFileBeanToDTO).toList();
    }
  }

  public static List<ProductCommentReadDTO> mapCommentsToDTO(List<ProductCommentBean> comments) {
    if (comments == null) {
      return new ArrayList<>();
    } else {
      return comments.stream().map(item -> new ProductCommentReadDTO(item)).toList();
    }
  }

  public static ProductFileDTO convertProductFileBeanToDTO(ProductFileBean productFileBean) {
    if (productFileBean == null) {
      return null;
    }
    return new ProductFileDTO(productFileBean);
  }

}

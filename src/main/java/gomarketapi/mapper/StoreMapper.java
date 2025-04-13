package gomarketapi.mapper;

import org.springframework.stereotype.Component;

import gomarketapi.dto.StoreDTO;
import gomarketapi.model.StoreBean;

@Component
public class StoreMapper {

  public static StoreBean toEntityBean(StoreDTO storeDTO) {
    StoreBean storeBean = new StoreBean();

    storeBean.setId(storeDTO.getId());
    storeBean.setName(storeDTO.getName());
    storeBean.setSlug(storeDTO.getSlug());
    storeBean.setIsActive(storeDTO.getIsActive());
    storeBean.setDescription(storeDTO.getDescription());

    return storeBean;
  }

  public static StoreDTO toDTO(StoreBean store) {
    StoreDTO storeDTO = new StoreDTO();

    storeDTO.setId(store.getId());
    storeDTO.setName(store.getName());
    storeDTO.setSlug(store.getSlug());
    storeDTO.setDescription(store.getDescription());
    storeDTO.setIsActive(store.getIsActive());
    storeDTO.setLikes(store.getLikes());

    storeDTO.setOwner(UserMapper.toDTO(store.getOwner()));

    if (store.getLogo() != null) {
      store.getLogo().setData(null);
      storeDTO.setLogo(store.getLogo());
    }

    storeDTO.setCreatedAt(store.getCreatedAt());
    storeDTO.setUpdatedAt(store.getUpdatedAt());

    return storeDTO;
  }
}

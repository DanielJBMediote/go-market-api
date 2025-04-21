package gomarketapi.mapper;

import org.springframework.stereotype.Component;

import gomarketapi.dto.store.StoreReadDTO;
import gomarketapi.model.StoreBean;

@Component
public class StoreMapper {

  public static StoreBean toEntityBean(StoreReadDTO storeDTO) {
    StoreBean storeBean = new StoreBean();

    storeBean.setId(storeDTO.getId());
    storeBean.setName(storeDTO.getName());
    storeBean.setSlug(storeDTO.getSlug());
    storeBean.setIsActive(storeDTO.getIsActive());
    storeBean.setDescription(storeDTO.getDescription());

    return storeBean;
  }

  public static StoreReadDTO toDTO(StoreBean storeBean) {
    StoreReadDTO store = new StoreReadDTO();

    store.setId(storeBean.getId());
    store.setName(storeBean.getName());
    store.setSlug(storeBean.getSlug());
    store.setDescription(storeBean.getDescription());
    store.setIsActive(storeBean.getIsActive());
    store.setLikes(storeBean.getLikes());

    store.setOwner(UserMapper.toDTO(storeBean.getOwner()));
    store.setLogo(FileMapper.toDTO(storeBean.getLogo()));

    store.setCreatedAt(storeBean.getCreatedAt());
    store.setUpdatedAt(storeBean.getUpdatedAt());

    return store;
  }
}

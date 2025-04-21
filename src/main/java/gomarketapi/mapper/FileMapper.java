package gomarketapi.mapper;

import org.springframework.stereotype.Component;

import gomarketapi.dto.FileDTO;
import gomarketapi.model.FileBean;

@Component
public class FileMapper {

  public static FileDTO toDTO(FileBean fileBean) {
    if (fileBean == null) {
      return null;
    }

    FileDTO file = new FileDTO();

    file.setId(fileBean.getId());
    file.setName(fileBean.getName());
    file.setSize(fileBean.getSize());
    file.setType(fileBean.getType());
    file.setExt(fileBean.getExt());
    file.setUuid(fileBean.getUuid());
    file.setCreatedAt(fileBean.getCreatedAt());
    file.setUpdatedAt(fileBean.getUpdatedAt());

    return file;
  }
}

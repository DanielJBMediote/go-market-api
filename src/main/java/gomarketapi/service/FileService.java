package gomarketapi.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import gomarketapi.exceptions.NotFoundException;
import gomarketapi.helpers.Where;
import gomarketapi.helpers.WhereBuilder;
import gomarketapi.model.FileBean;
import gomarketapi.repository.FileRepository;

@Service
public class FileService {

  private final FileRepository fileRepository;

  public FileService(FileRepository fileRepository) {
    this.fileRepository = fileRepository;
  }

  public FileBean createFileBean(MultipartFile file) throws IOException {
    String originalFilename = StringUtils.hasText(file.getOriginalFilename())
        ? file.getOriginalFilename()
        : UUID.randomUUID().toString();

    String extension = getExtension(file);
    if (extension.isEmpty()) {
      extension = determineFileExtension(file.getContentType());
    }

    FileBean fileBean = new FileBean();
    fileBean.setName(originalFilename);
    fileBean.setSize(BigDecimal.valueOf(file.getSize()));
    fileBean.setExt(extension);
    fileBean.setType(file.getContentType());
    fileBean.setUuid(UUID.randomUUID().toString());
    // fileBean.setData(IOUtils.toByteArray(file.getInputStream()));
    String base64 = Base64.getEncoder().encodeToString(IOUtils.toByteArray(file.getInputStream()));

    // System.out.println("Tamanho do arquivo (bytes): " +
    // fileBean.getData().length);

    fileBean.setBase64Data(base64);

    return fileBean;
  }

  private String getExtension(MultipartFile file) {
    String originalFilename = file.getOriginalFilename();
    if (originalFilename == null)
      originalFilename = UUID.randomUUID().toString();
    return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
  }

  private String determineFileExtension(String contentType) {
    if (contentType == null)
      return "bin";

    return switch (contentType) {
      case "image/jpeg" -> "jpg";
      case "image/png" -> "png";
      case "application/pdf" -> "pdf";
      default -> "bin";
    };
  }

  public FileBean getByUUID(String uuid) {
    WhereBuilder<FileBean> whereBuilder = new WhereBuilder<>(FileBean.class);
    whereBuilder.addWhere(new Where("uuid", uuid, Where.Operator.EQUALS));
    Specification<FileBean> spec = whereBuilder.build();

    FileBean fileBean = this.fileRepository.findOne(spec)
        .orElseThrow(() -> new NotFoundException("File not found with uuid " + uuid));

    if (fileBean.getBase64Data() == null) {
      throw new RuntimeException("File does not have any data.");
    }

    return fileBean;
  }

  public void delete(Long id) {
    FileBean fileBean = this.fileRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("File not found with id " + id));
    this.fileRepository.delete(fileBean);
  }
}
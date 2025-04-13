package gomarketapi.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "files")
public class FileBean extends Bean {

  private String name;
  private String uuid;
  private String ext;
  private BigDecimal size;
  private String type;

  @Lob
  @Column(name = "data", columnDefinition = "LONGBLOB")
  private byte[] data;

  @Column(columnDefinition = "LONGTEXT")
  private String base64Data;

  public FileBean() {
  }

  public FileBean(Long id) {
    super(id);
  }

  public FileBean(String name, String uuid, String ext, BigDecimal size, String type) {
    this.name = name;
    this.uuid = uuid;
    this.ext = ext;
    this.size = size;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getExt() {
    return ext;
  }

  public void setExt(String ext) {
    this.ext = ext;
  }

  public BigDecimal getSize() {
    return size;
  }

  public void setSize(BigDecimal size) {
    this.size = size;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public String getBase64Data() {
    return base64Data;
  }

  public void setBase64Data(String base64Data) {
    this.base64Data = base64Data;
  }
}

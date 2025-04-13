package gomarketapi.controller;

import java.util.Base64;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gomarketapi.model.FileBean;
import gomarketapi.service.FileService;

@RestController
@RequestMapping("/api/files")
public class FileController {

  private final FileService fileService;

  FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<byte[]> getByUUID(@PathVariable String uuid) {
    FileBean fileBean = fileService.getByUUID(uuid);

    byte[] decodedBytes = Base64.getDecoder().decode(fileBean.getBase64Data());

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(fileBean.getType()))
        .body(decodedBytes);
  }

}

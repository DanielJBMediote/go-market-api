package gomarketapi.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gomarketapi.dto.store.StoreCreateDTO;
import gomarketapi.service.StoreService;

@RestController
@RequestMapping("/api/stores")
public class StoreControlller {

  private final StoreService storeService;

  StoreControlller(StoreService storeService) {
    this.storeService = storeService;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> create(
      @RequestPart(value = "data") StoreCreateDTO body,
      @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
    return this.storeService.create(body, file);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) throws Exception {
    return this.storeService.getById(id);
  }

  @GetMapping("/{id}/dashboard-metrics")
  public ResponseEntity<?> getDashboardMetricsByStoreId(@PathVariable Long id) {
    return this.storeService.getDashboardMetricsByStoreId(id);
  }

  @GetMapping("/dashboard-metrics")
  public ResponseEntity<?> getDashboardMetrics() {
    return this.storeService.getDashboardMetrics();
  }

  @GetMapping
  public ResponseEntity<?> getAll(@RequestParam(required = false) String where) throws Exception {
    return this.storeService.getAll(where);
  }

  @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> update(
      @PathVariable Long id,
      @RequestPart("data") StoreCreateDTO body,
      @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
    return this.storeService.update(id, body, file);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
    return this.storeService.delete(id);
  }

}

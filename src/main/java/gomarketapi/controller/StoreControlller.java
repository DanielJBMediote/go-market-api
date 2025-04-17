package gomarketapi.controller;

import java.util.List;

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

import gomarketapi.dto.StoreDashboardMetricsDTO;
import gomarketapi.dto.store.StoreCreateDTO;
import gomarketapi.dto.store.StoreReadDTO;
import gomarketapi.helpers.ResponseBuilder;
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
    try {
      StoreReadDTO store = this.storeService.create(body, file);
      return ResponseBuilder.success("Store has been created sucessfully!", store);
    } catch (Exception e) {
      return ResponseBuilder.badRequest("Failed to create store. Cause: " + e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) throws Exception {
    StoreReadDTO store = this.storeService.getById(id);
    return ResponseBuilder.success(store);
  }

  @GetMapping("/{id}/dashboard-metrics")
  public ResponseEntity<?> getDashboardMetricsByStoreId(@PathVariable Long id) {
    try {
      StoreDashboardMetricsDTO data = this.storeService.getDashboardMetricsByStoreId(id);
      return ResponseBuilder.success(data);
    } catch (Exception e) {
      return ResponseBuilder.badRequest(e.getMessage());
    }
  }

  @GetMapping("/dashboard-metrics")
  public ResponseEntity<?> getDashboardMetrics() {
    try {
      StoreDashboardMetricsDTO data = this.storeService.getDashboardMetrics();
      return ResponseBuilder.success(data);
    } catch (Exception e) {
      return ResponseBuilder.badRequest(e.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<?> getAll(@RequestParam(required = false) String where) throws Exception {
    List<StoreReadDTO> list = this.storeService.getAll(where);
    return ResponseBuilder.success(list);
  }

  @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> update(
      @PathVariable Long id,
      @RequestPart("data") StoreCreateDTO body,
      @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
    try {
      StoreReadDTO store = this.storeService.update(id, body, file);
      return ResponseBuilder.success("Store has been updated sucessfully!", store);
    } catch (Exception e) {
      return ResponseBuilder.badRequest("Failed to update Store. Cause: " + e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
    try {
      this.storeService.delete(id);
      return ResponseBuilder.success("Store has been deleted sucessfully!");
    } catch (Exception e) {
      return ResponseBuilder.badRequest("Failed to delete Store. Cause: " + e.getMessage());
    }
  }

}

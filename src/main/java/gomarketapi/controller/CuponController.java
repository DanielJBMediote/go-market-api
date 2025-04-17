package gomarketapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gomarketapi.dto.cupons.CuponCreateDTO;
import gomarketapi.dto.cupons.CuponReadDTO;
import gomarketapi.helpers.ResponseBuilder;
import gomarketapi.service.CuponService;

@RestController
@RequestMapping("/api/cupons")
public class CuponController {

  private final CuponService cuponService;

  CuponController(ResponseBuilder responseBuilder, CuponService cuponService) {
    this.cuponService = cuponService;
  }

  @PostMapping
  public ResponseEntity<?> post(@RequestBody CuponCreateDTO body) {
    try {
      CuponReadDTO cupon = this.cuponService.create(body);
      return ResponseBuilder.success("Cupon created sucessfully!", cupon);
    } catch (Exception e) {
      return ResponseBuilder.badRequest("Failed to create cupon. Cause " + e.getMessage());
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CuponCreateDTO body) {
    try {
      CuponReadDTO cupon = this.cuponService.update(id, body);
      return ResponseBuilder.success("Cupon updated sucessfully!", cupon);
    } catch (Exception e) {
      return ResponseBuilder.badRequest("Failed to create cupon. Cause " + e.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<?> getAll(@RequestParam String where) {
    List<CuponReadDTO> list = this.cuponService.getAll(where);
    return ResponseBuilder.success(list);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) {
    CuponReadDTO cupon = this.cuponService.getById(id);
    return ResponseBuilder.success(cupon);
  }

  @GetMapping("/use/{code}")
  public ResponseEntity<?> use(@PathVariable String code) {
    CuponReadDTO cupon = this.cuponService.useCuponByCode(code);
    return ResponseBuilder.success("Cupon has been applied!", cupon);
  }

}

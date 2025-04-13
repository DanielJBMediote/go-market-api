package gomarketapi.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gomarketapi.dto.UserDTO;
import gomarketapi.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/sign-up")
  public ResponseEntity<UserDTO> signUp(@RequestBody UserDTO userDTO) {
    return ResponseEntity.ok(authService.register(userDTO));
  }

  @PostMapping("/sign-in")
  public ResponseEntity<UserDTO> signIn(@RequestBody Map<String, String> credentials) {
    String username = credentials.get("username");
    String password = credentials.get("password");
    return ResponseEntity.ok(authService.authenticate(username, password));
  }

  @GetMapping("/me")
  public UserDTO getMe() {
    return this.authService.getMe();
  }

}
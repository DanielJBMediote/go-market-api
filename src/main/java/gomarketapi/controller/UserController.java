package gomarketapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gomarketapi.dto.UserDTO;
import gomarketapi.helpers.ResponseBuilder;
import gomarketapi.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseBuilder.success(user);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) String where) {
        List<UserDTO> users = this.userService.getUsers(where);
        return ResponseBuilder.success(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserDTO body) throws Exception {
        UserDTO user = this.userService.update(id, body);
        return ResponseBuilder.success(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'.");
    }

}
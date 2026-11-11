package gomarketapi.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import gomarketapi.contexts.ApiResponse;

@RestControllerAdvice
public class ResponseBuilder {

  public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
    ApiResponse<T> response = new ApiResponse<>(HttpStatus.OK.value(), message, data);
    return ResponseEntity.ok(response);
  }

  public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
    ApiResponse<T> response = new ApiResponse<>(HttpStatus.OK.value(), null, data);
    return ResponseEntity.ok(response);
  }

  public static <T> ResponseEntity<ApiResponse<T>> success(String message) {
    ApiResponse<T> response = new ApiResponse<>(HttpStatus.OK.value(), message, null);
    return ResponseEntity.ok(response);
  }

  public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message) {
    ApiResponse<T> response = new ApiResponse<>(HttpStatus.FORBIDDEN.value(), message, null);
    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

  public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
    ApiResponse<T> response = new ApiResponse<>(HttpStatus.CREATED.value(), message, data);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  public static ResponseEntity<ApiResponse<Object>> error(HttpStatus status, String message) {
    ApiResponse<Object> response = new ApiResponse<>(status.value(), message, null);
    return new ResponseEntity<>(response, status);
  }

  public static ResponseEntity<ApiResponse<Object>> badRequest(String message) {
    return error(HttpStatus.BAD_REQUEST, message);
  }

  public static ResponseEntity<ApiResponse<Object>> notFound(String message) {
    return error(HttpStatus.NOT_FOUND, message);
  }

  public static ResponseEntity<ApiResponse<Object>> internalServerError(String message) {
    return error(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }
}

package gomarketapi.filter;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import gomarketapi.config.AuthenticatedUser;
import gomarketapi.contexts.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserContextFilter extends OncePerRequestFilter {

  private final UserContext userContext;

  public UserContextFilter(UserContext userContext) {
    this.userContext = userContext;
  }

  @SuppressWarnings("null")
  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    var authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();

      if (principal instanceof AuthenticatedUser) {
        AuthenticatedUser userDetails = (AuthenticatedUser) principal;
        userContext.setUserId(userDetails.getUserId());
        userContext.setUsername(userDetails.getUsername());
      }
    }

    try {
      filterChain.doFilter(request, response);
    } finally {
      userContext.clear();
    }
  }
}
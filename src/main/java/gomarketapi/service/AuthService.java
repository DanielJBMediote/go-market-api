package gomarketapi.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gomarketapi.contexts.UserContext;
import gomarketapi.dto.UserDTO;
import gomarketapi.exceptions.NotFoundException;
import gomarketapi.helpers.Where;
import gomarketapi.helpers.WhereBuilder;
import gomarketapi.mapper.UserMapper;
import gomarketapi.model.UserBean;
import gomarketapi.repository.UserRepository;

@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final UserContext userContext;

  public AuthService(
      AuthenticationManager authenticationManager,
      UserRepository userService,
      JwtService jwtService,
      UserContext userContext) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userService;
    this.jwtService = jwtService;
    this.userContext = userContext;
  }

  public UserDTO authenticate(String username, String password) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, password));

    if (authentication != null) {
      WhereBuilder<UserBean> whereBuilder = new WhereBuilder<>(UserBean.class);
      whereBuilder.addWhere(new Where("username", username, Where.Operator.EQUALS));
      Specification<UserBean> spec = whereBuilder.build();
      UserBean userBean = userRepository.findOne(spec)
          .orElseThrow(() -> new NotFoundException("User not found with this username!"));

      String token = jwtService.generateToken(userBean);
      userBean.setAccessKey(token);

      return UserMapper.toDTO(userBean);
    }
    return null;

  }

  public UserDTO register(UserDTO userDTO) {
    UserService userService = new UserService(userRepository, userContext);
    return userService.createUser(userDTO);
  }

  public UserDTO getMe() {
    Long userId = userContext.getUserId();

    UserBean userBean = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("User " + userId + " not found."));

    String token = jwtService.generateToken(userBean);
    userBean.setAccessKey(token);

    return UserMapper.toDTO(userBean);
  }
}
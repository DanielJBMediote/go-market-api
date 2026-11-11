package gomarketapi.mapper;

import org.springframework.stereotype.Component;

import gomarketapi.dto.UserDTO;
import gomarketapi.model.UserBean;

@Component
public class UserMapper {

  // @Autowired
  // private UserRepository userRepository;

  public static UserBean toEntityBean(UserDTO userDTO) {
    UserBean user = new UserBean();

    user.setName(userDTO.getName());
    user.setEmail(userDTO.getEmail());
    // user.setPassword(userDTO.getPassword());
    user.setUsername(userDTO.getUsername());
    user.setRole(userDTO.getRole());
    return user;
  }

  public static UserDTO toDTO(UserBean user) {
    if (user != null)
      return new UserDTO(user);
    else
      return new UserDTO();
  }
}

package gomarketapi.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import gomarketapi.contexts.UserContext;
import gomarketapi.dto.UserDTO;
import gomarketapi.exceptions.EmailAlreadyExistsException;
import gomarketapi.exceptions.ForbiddenException;
import gomarketapi.exceptions.NotFoundException;
import gomarketapi.helpers.WhereBuilder;
import gomarketapi.mapper.UserMapper;
import gomarketapi.model.UserBean;
import gomarketapi.repository.UserRepository;
import gomarketapi.utils.PasswordUtils;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserContext userContext;

	public UserService(UserRepository userRepository, UserContext userContext) {
		this.userRepository = userRepository;
		this.userContext = userContext;
	}

	public UserDTO createUser(UserDTO userDTO) {
		UserBean userBean = UserMapper.toEntityBean(userDTO);
		
		if (userRepository.existsByEmail(userBean.getEmail())) {
			throw new EmailAlreadyExistsException("Este e-mail já esta cadastrado!");
		}
		
		String password = userDTO.getPassword();
		String hashPassword = PasswordUtils.hashPassword(password);
		userBean.setPassword(hashPassword);

		UserBean savedUser = userRepository.save(userBean);
		
		return UserMapper.toDTO(savedUser);
	}

	public UserDTO getUserById(Long id) {
		UserBean user = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User not found with id: " + id + "!"));
		return UserMapper.toDTO(user);
	}

	public List<UserDTO> getUsers(String whereEncodedURI) {
		WhereBuilder<UserBean> whereBuilder = new WhereBuilder<>(UserBean.class);
		Specification<UserBean> spec = whereBuilder.parseWhereEncodedURIParams(whereEncodedURI).build();
		List<UserBean> userList = this.userRepository.findAll(spec);

		return userList.stream().map(UserMapper::toDTO).toList();
	}

	public UserDTO update(Long id, UserDTO userDTO) throws Exception {
		UserBean userBean = this.userRepository.findById(id).orElse(null);
		if (userBean == null) {
			throw new NotFoundException("User not found!");
		}

		if (userBean.getId() != userContext.getUserId()) {
			throw new ForbiddenException("You don't have permission to update this User!");
		}

		userBean.setName(userDTO.getName());
		userBean.setEmail(userDTO.getEmail());
		userBean.setUsername(userDTO.getUsername());

		String password = userDTO.getPassword();
		if (password != null) {
			String hashedPassword = PasswordUtils.hashPassword(password);
			userBean.setPassword(hashedPassword);
		}

		this.userRepository.save(userBean);

		return UserMapper.toDTO(userBean);
	}

	public ResponseEntity<?> delete(Long id) {
		throw new Error("Method not implemented.");
		// this.userRepository.deleteById(id);
	}

}
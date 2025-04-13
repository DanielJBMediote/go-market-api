package gomarketapi.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import gomarketapi.config.AuthenticatedUser;
import gomarketapi.model.UserBean;
import gomarketapi.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserBean userBean = userRepository.findByUsername(username) // Adicione este método no UserRepository
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new AuthenticatedUser(
                userBean.getId(),
                userBean.getUsername(),
                userBean.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }

}
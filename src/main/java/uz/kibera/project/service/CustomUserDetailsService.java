package uz.kibera.project.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.kibera.project.dao.CustomUserDetails;
import uz.kibera.project.dao.entity.User;
import uz.kibera.project.dao.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<User> user = userRepository.findByUsername(username);

            if (user.isEmpty()) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            log.info("IN loadUserByUsername - user with username: {} successfully loaded", username);
            return new CustomUserDetails(user.get());
    }
}

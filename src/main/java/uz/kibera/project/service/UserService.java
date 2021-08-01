package uz.kibera.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.kibera.project.configuration.jwt.JwtTokenProvider;
import uz.kibera.project.dao.entity.User;
import uz.kibera.project.dao.repository.UserRepository;
import uz.kibera.project.dto.AccessTokenResponse;
import uz.kibera.project.dto.AuthenticationRequest;

import javax.security.auth.message.AuthException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = getUserByName(login);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with username: " + login + " not found");
        }

        log.info("IN loadUserByUsername - user with username: {} successfully loaded", login);
        return (UserDetails) user.get();
    }

    public Optional<User> getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public AccessTokenResponse authenticate(AuthenticationRequest authenticationRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            User user = getUserByName(authentication.getName()).orElseThrow();
            return AccessTokenResponse.builder()
                    .accessToken(jwtTokenProvider.createToken(user.getUsername(), user.getRole()))
                    .build();
        } catch (AuthenticationException exception) {
                throw new BadCredentialsException("Incorrect username or password");
        }
    }
}

package uz.kibera.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.kibera.project.configuration.jwt.JwtTokenProvider;
import uz.kibera.project.dao.entity.User;
import uz.kibera.project.dao.repository.UserRepository;
import uz.kibera.project.dto.AccessTokenResponse;
import uz.kibera.project.dto.AuthenticationRequest;
import uz.kibera.project.dto.RegistrationRequest;
import uz.kibera.project.dto.UserDto;
import uz.kibera.project.exception.UserNameAlreadyExistException;
import uz.kibera.project.mapper.UserMapper;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public AccessTokenResponse authenticate(AuthenticationRequest authenticationRequest){
        try {
            log.info("hello");
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            User user = getUserByUsername(authentication.getName()).orElseThrow();
            return AccessTokenResponse.builder()
                    .accessToken(jwtTokenProvider.createToken(user.getUsername(), user.getRole(), user.getId()))
                    .build();
        } catch (AuthenticationException exception) {
                throw new BadCredentialsException("Incorrect username or password");
        }
    }

    public AccessTokenResponse register(RegistrationRequest registrationRequest) {
        log.info("Handled request for register");
        if(getUserByUsername(registrationRequest.getUsername()).isPresent()) {
            throw new UserNameAlreadyExistException("This username already exist");
        }

        User user= User.builder()
                .username(registrationRequest.getUsername())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .locked(false)
                .email(registrationRequest.getEmail())
                .role(registrationRequest.getRole())
                .build();
        userRepository.save(user);
        log.info("User by {} username successfully registered", user.getUsername());
        return AccessTokenResponse.builder()
                .accessToken(jwtTokenProvider.createToken(user.getUsername(), user.getRole(), user.getId()))
                .build();
    }

    public Page<UserDto> fetchUsers(Pageable pageable) {

        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(userMapper::tUserDto);
    }
}

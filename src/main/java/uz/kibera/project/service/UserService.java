package uz.kibera.project.service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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
import uz.kibera.project.dto.*;
import uz.kibera.project.exception.EmailAlreadyExistException;
import uz.kibera.project.exception.UserHaveNotAccess;
import uz.kibera.project.exception.UserNameAlreadyExistException;
import uz.kibera.project.exception.UserNotFoundException;
import uz.kibera.project.mapper.UserMapper;

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

    public UserResponseWithAccessToken authenticate(AuthenticationRequest authenticationRequest) {
        log.info("Request handled for authenticate user with {} username", authenticationRequest.getUsername());
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

            User user = getUserByUsername(authentication.getName()).orElseThrow(() -> {
                throw new UserNotFoundException();
            });

            return UserResponseWithAccessToken.builder()
                    .userResponse(userMapper.toUserResponse(user))
                    .accessToken(jwtTokenProvider.createToken(user.getUsername(), user.getRole(), user.getId()))
                    .build();

        } catch (AuthenticationException exception) {
            throw new BadCredentialsException("Incorrect username or password");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public AccessTokenResponse register(RegistrationRequest registrationRequest) {
        log.info("Handled request for register");
        if (getUserByUsername(registrationRequest.getUsername()).isPresent()) {
            throw new UserNameAlreadyExistException("This username already exist");
        }

        checkEmail(registrationRequest.getEmail());

        User user = User.builder()
                .username(registrationRequest.getUsername())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .locked(false)
                .email(registrationRequest.getEmail())
                .role(registrationRequest.getRole())
                .build();
        user = userRepository.save(user);
        log.info("User by {} username successfully registered", user.getUsername());

        return AccessTokenResponse.builder()
                .accessToken(jwtTokenProvider.createToken(user.getUsername(), user.getRole(), user.getId()))
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public Page<UserResponse> fetchUsers(Pageable pageable) {

        Page<User> userPage = userRepository.findAllByDeletedIsFalse(pageable);
        return userPage.map(userMapper::toUserResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    public void makeDisableOrEnable(Long id) {
        log.info("Request handled for enabling or disabling user with {} id", id);
        User user = fetchUser(id);
        user.setLocked(!user.getLocked());
        userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public User fetchUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id).orElseThrow(() -> {
            log.error("User not found with {} id", id);
            throw new UserNotFoundException();
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponse updateUserEntity(Long id, UpdatingUserDto updatingUserDto) {
        log.info("Request handled for updating user");
        User user = fetchUser(id);
        userMapper.updateEntity(user, updatingUserDto);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(Long id, UpdatingUserRequest userRequest) {

        if(id.equals(1L)) {
            throw new UserHaveNotAccess();
        }

        User updatableUser = fetchUser(id);
        String password = ObjectUtils.isNotEmpty(userRequest.getPassword()) ? passwordEncoder.encode(userRequest.getPassword()) : updatableUser.getPassword();
        updatableUser.setFirstName(userRequest.getFirstName());
        updatableUser.setLastName(userRequest.getLastName());
        updatableUser.setRole(userRequest.getRole());
        updatableUser.setPassword(password);
        log.info("Updated User by {} id", id);
        return userMapper.toUserResponse(userRepository.save(updatableUser));
    }

    private void checkEmail(String email) {
        if (userRepository.findAll().stream().anyMatch(user -> user.getEmail().equals(email))) {
            throw new EmailAlreadyExistException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {

        if (id.equals(1L)) {
           throw new UserHaveNotAccess();
        }

        User deletingUser = fetchUser(id);
        deletingUser.setDeleted(true);
        deletingUser.setDeletedAt(LocalDateTime.now());
        log.info("Deleted User with {} id at {}", id, deletingUser.getDeletedAt());
        userRepository.save(deletingUser);
    }
}

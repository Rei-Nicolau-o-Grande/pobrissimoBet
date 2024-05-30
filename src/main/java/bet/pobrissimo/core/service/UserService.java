package bet.pobrissimo.core.service;

import bet.pobrissimo.core.dto.user.UserCreateDto;
import bet.pobrissimo.core.dto.user.UserResponseDto;
import bet.pobrissimo.core.enums.RoleEnum;
import bet.pobrissimo.core.model.Role;
import bet.pobrissimo.core.model.User;
import bet.pobrissimo.core.repository.RoleRepository;
import bet.pobrissimo.core.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void create(UserCreateDto dto) {
        Role defaultRole = roleRepository.findById(RoleEnum.PLAYER.getId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User(dto, passwordEncoder.encode(dto.password()), defaultRole);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> shearch(String username, String email, String role, Pageable pageable) {

        if (username == null && email == null && role == null) {
            return this.findAllPageable(pageable);
        }

        Page<User> users = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrRolesName
                (username, email, role, pageable);

        if (users.isEmpty()) {
            this.findAllPageable(pageable);
        }

        return getUserResponseDtos(pageable, users);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> findAllPageable(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return getUserResponseDtos(pageable, users);
    }

    private Page<UserResponseDto> getUserResponseDtos(Pageable pageable, Page<User> users) {
        List<UserResponseDto> userDtos = users.stream()
                .map(user -> new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())))
                .collect(Collectors.toList());
        return new PageImpl<>(userDtos, pageable, users.getTotalElements());
    }

}

package bet.pobrissimo.core.service;

import bet.pobrissimo.core.specifications.UserSpecifications;
import bet.pobrissimo.core.dto.user.MeResponseDto;
import bet.pobrissimo.core.dto.user.UserRequestDto;
import bet.pobrissimo.core.dto.user.UserResponseDto;
import bet.pobrissimo.core.enums.RoleEnum;
import bet.pobrissimo.core.model.Role;
import bet.pobrissimo.core.model.User;
import bet.pobrissimo.core.repository.RoleRepository;
import bet.pobrissimo.core.repository.UserRepository;
import bet.pobrissimo.infra.config.AccessControlService;
import bet.pobrissimo.infra.config.AuthenticatedCurrentUser;
import bet.pobrissimo.infra.exception.EntityNotFoundException;
import bet.pobrissimo.infra.util.ValidateConvertStringToUUID;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
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
    public void create(UserRequestDto dto) {
        Role rolePlayer = this.roleRepository.findById(RoleEnum.PLAYER.getId())
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada."));
        User user = new User(dto, passwordEncoder.encode(dto.password()), rolePlayer);
        userRepository.save(user);
    }

    @Transactional
    public void update(String userId, UserRequestDto dto) {
        this.findById(userId);
        AccessControlService.checkPermission(userId);
        User user = this.userRepository.getReferenceById(UUID.fromString(userId));
        BeanUtils.copyProperties(dto, user, "password");

        if (dto.password() != null && !dto.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        this.userRepository.save(user);
    }

    @Transactional
    public void delete(String userId) {
        var user = this.findById(userId);
        AccessControlService.checkPermission(userId);
        user.setDeactivatedAt(Instant.now());
        user.setActive(false);
    }

    @Transactional(readOnly = true)
    public User findById(String userId) {
        var userIdUUID = ValidateConvertStringToUUID.validate(userId, "Usuário não encontrado.");

        return this.userRepository.findById(userIdUUID)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> search(String username, String email, Boolean isActive, String role, Pageable pageable) {

        // Log dos parâmetros recebidos no serviço
        System.out.println("Service - Username: " + username + ", Email: " + email +
                ", isActive: " + isActive + ", Role: " + role);

        Specification<User> specification = UserSpecifications.searchByCriteria(username, email, isActive, role);

        Page<User> users = this.userRepository.findAll(specification, pageable);

        return getUserResponseDtos(pageable, users);
    }

    private Page<UserResponseDto> getUserResponseDtos(Pageable pageable, Page<User> users) {
        List<UserResponseDto> userDtos = users.stream()
                .map(user -> new UserResponseDto(
                        user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt(), user.isActive(),
                        user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())))
                .collect(Collectors.toList());
        return new PageImpl<>(userDtos, pageable, users.getTotalElements());
    }

    public MeResponseDto me() {
        UUID userId = AuthenticatedCurrentUser.getUserId();
        String userName = AuthenticatedCurrentUser.getUserName();
        String email = AuthenticatedCurrentUser.getUserEmail();

        return new MeResponseDto(userId, userName, email);
    }
}

package bet.pobrissimo.core.service;

import bet.pobrissimo.core.dtos.user.MeResponseDto;
import bet.pobrissimo.core.dtos.user.UserRequestDto;
import bet.pobrissimo.core.dtos.user.UserResponseDto;
import bet.pobrissimo.core.validators.ValidationUserService;
import bet.pobrissimo.core.dtos.wallet.MyWalletResponseDto;
import bet.pobrissimo.core.enums.RoleEnum;
import bet.pobrissimo.core.model.Role;
import bet.pobrissimo.core.model.User;
import bet.pobrissimo.core.repository.RoleRepository;
import bet.pobrissimo.core.repository.UserRepository;
import bet.pobrissimo.core.repository.specifications.UserSpecifications;
import bet.pobrissimo.infra.config.AccessControlService;
import bet.pobrissimo.infra.config.AuthenticatedCurrentUser;
import bet.pobrissimo.infra.exception.AccessDeniedException;
import bet.pobrissimo.infra.exception.EntityNotFoundException;
import bet.pobrissimo.infra.util.ValidateConvertStringToUUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final WalletService walletService;
    private final ValidationUserService validationUserService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository,
                       WalletService walletService,
                       ValidationUserService validationUserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.walletService = walletService;
        this.validationUserService = validationUserService;
    }

    @Transactional
    public void create(UserRequestDto dto) {
        Role rolePlayer = this.roleRepository.findById(RoleEnum.PLAYER.getId())
                .orElseThrow(() -> new EntityNotFoundException("Role não encontrada."));

        this.validationUserService.validateUser(dto);

        User user = new User(dto, passwordEncoder.encode(dto.password()), rolePlayer);
        this.walletService.create(user);
        this.userRepository.save(user);
    }

    @Transactional
    public void update(String userId, UserRequestDto dto) {
        User user = this.findById(userId);
        AccessControlService.checkPermission(userId);

        if (!dto.username().isEmpty() && !dto.username().equals(user.getUsername())) {
            this.validationUserService.validateUsername(dto.username());
            user.setUsername(dto.username());
        }

        if (!dto.email().isEmpty() && !dto.email().equals(user.getEmail())) {
            this.validationUserService.validateEmail(dto.email());
            user.setEmail(dto.email());
        }

        if (!dto.password().isEmpty()) {
            this.validationUserService.validatePassword(dto.password());
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

        Optional<User> userOptional = this.userRepository.findById(userIdUUID);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals(RoleEnum.ADMIN.getName()));

            if (isAdmin) {
                throw new AccessDeniedException("Admin não pode acessar esse recurso.");
            }

            return user;
        } else {
            throw new EntityNotFoundException("Usuário não encontrado.");
        }
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> search(String username, String email, Boolean isActive, Pageable pageable) {
        Specification<User> specification = UserSpecifications.searchByCriteria(username, email, isActive);

        Page<User> users = this.userRepository.findAll(specification, pageable);

        return getUserResponseDtos(pageable, users);
    }

    private Page<UserResponseDto> getUserResponseDtos(Pageable pageable, Page<User> users) {
        List<UserResponseDto> userDtos = users.stream()
                .map(user -> new UserResponseDto(
                        user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt(),
                user.isActive(), new MyWalletResponseDto(user.getWallet().getId(), user.getWallet().getAmount()),
                        user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())))
                .collect(Collectors.toList());
        return new PageImpl<>(userDtos, pageable, users.getTotalElements());
    }

    @Transactional(readOnly = true)
    public MeResponseDto me() {
        var user = this.findById(AuthenticatedCurrentUser.getUserId().toString());

        UUID userId = user.getId();
        String userName = user.getUsername();
        String email = user.getEmail();

        return new MeResponseDto(userId, userName, email);
    }
}

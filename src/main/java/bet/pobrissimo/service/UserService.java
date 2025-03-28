package bet.pobrissimo.service;

import bet.pobrissimo.dtos.user.MeResponseDto;
import bet.pobrissimo.dtos.user.UserRequestDto;
import bet.pobrissimo.dtos.user.UserResponseDto;
import bet.pobrissimo.validators.ValidationUserService;
import bet.pobrissimo.dtos.wallet.MyWalletResponseDto;
import bet.pobrissimo.enums.RoleEnum;
import bet.pobrissimo.model.Role;
import bet.pobrissimo.model.User;
import bet.pobrissimo.repository.RoleRepository;
import bet.pobrissimo.repository.UserRepository;
import bet.pobrissimo.repository.specifications.UserSpecifications;
import bet.pobrissimo.config.AccessControlService;
import bet.pobrissimo.config.AuthenticatedCurrentUser;
import bet.pobrissimo.exception.exceptions.AccessDeniedException;
import bet.pobrissimo.exception.exceptions.EntityNotFoundException;
import bet.pobrissimo.util.ValidateConvertStringToUUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
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
                .orElseThrow(() -> new EntityNotFoundException(
                        HttpStatus.NOT_FOUND,
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        "Role não encontrada."
                ));

        this.validationUserService.validateUserForCreate(dto);

        User user = new User(dto, passwordEncoder.encode(dto.password()), rolePlayer);
        this.walletService.create(user);
        this.userRepository.save(user);
    }

    @Transactional
    public void update(String userId, UserRequestDto dto) {
        User user = this.findById(userId);
        AccessControlService.checkPermission(userId);

        this.validationUserService.validateUserForUpdate(dto, user);

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
                throw new AccessDeniedException(
                        HttpStatus.FORBIDDEN,
                        HttpStatus.FORBIDDEN.value(),
                        HttpStatus.FORBIDDEN.getReasonPhrase(),
                        "Admin não pode acessar esse recurso."
                );
            }

            return user;
        } else {
            throw new EntityNotFoundException(
                    HttpStatus.NOT_FOUND,
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "Usuário não encontrado."
            );
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
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getCreatedAt(),
                        user.isActive(),
                        new MyWalletResponseDto(user.getWallet().getId(), user.getWallet().getAmount()),
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

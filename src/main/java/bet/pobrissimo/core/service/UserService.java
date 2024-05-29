package bet.pobrissimo.core.service;

import bet.pobrissimo.core.dto.user.UserCreateDto;
import bet.pobrissimo.core.enums.RoleEnum;
import bet.pobrissimo.core.model.Role;
import bet.pobrissimo.core.model.User;
import bet.pobrissimo.core.repository.RoleRepository;
import bet.pobrissimo.core.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}

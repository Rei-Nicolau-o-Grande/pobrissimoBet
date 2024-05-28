package bet.pobrissimo.core.service;

import bet.pobrissimo.core.dto.user.UserCreateDto;
import bet.pobrissimo.core.model.User;
import bet.pobrissimo.core.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void create(UserCreateDto dto) {
        this.userRepository.save(new User(dto, this.passwordEncoder.encode(dto.password())));
    }
}

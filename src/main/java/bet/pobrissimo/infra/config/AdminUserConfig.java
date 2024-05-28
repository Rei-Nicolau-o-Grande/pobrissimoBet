package bet.pobrissimo.infra.config;

import bet.pobrissimo.core.model.User;
import bet.pobrissimo.core.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var user = this.userRepository.findByEmail("teste1teste.com");

        user.ifPresentOrElse(
                userCreate -> System.out.println("Usuário já cadastrado"),
                () -> {
                    var userCreate = new User();
                    userCreate.setName("Teste 1");
                    userCreate.setEmail("teste1teste.com");
                    userCreate.setPassword(passwordEncoder.encode("123456"));
                    this.userRepository.save(userCreate);
                }
        );
    }
}

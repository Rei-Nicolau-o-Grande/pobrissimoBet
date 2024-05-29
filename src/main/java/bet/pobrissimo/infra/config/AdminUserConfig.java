package bet.pobrissimo.infra.config;

import bet.pobrissimo.core.model.User;
import bet.pobrissimo.core.enums.RoleEnum;
import bet.pobrissimo.core.repository.RoleRepository;
import bet.pobrissimo.core.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(UserRepository userRepository,
                            RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var user = this.userRepository.findByEmail("teste1@teste.com");

        var roleAdmin = this.roleRepository.findByName(RoleEnum.ADMIN.getName());

        user.ifPresentOrElse(
                userCreate -> System.out.println("Usuário já cadastrado"),
                () -> {
                    var userCreate = new User();
                    userCreate.setUsername("Teste 1");
                    userCreate.setEmail("teste1@teste.com");
                    userCreate.setRoles(Set.of(roleAdmin));
                    userCreate.setPassword(passwordEncoder.encode("123456"));
                    this.userRepository.save(userCreate);
                }
        );
    }
}

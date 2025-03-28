package bet.pobrissimo.seeders.user;

import bet.pobrissimo.enums.RoleEnum;
import bet.pobrissimo.model.User;
import bet.pobrissimo.repository.RoleRepository;
import bet.pobrissimo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

@Configuration
public class AdminSeeder implements CommandLineRunner {

    @Value("${app.admin.email}")
    private String ADMIN_EMAIL;

    @Value("${app.admin.username}")
    private String ADMIN_USERNAME;

    @Value("${app.admin.password}")
    private String ADMIN_PASSWORD;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AdminSeeder(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedAdmin();
    }

    private void seedAdmin() {
        var admin = this.userRepository.findByEmail(ADMIN_EMAIL);
        var roleAdmin = this.roleRepository.findByName(RoleEnum.ADMIN.getName());

        admin.ifPresentOrElse(
                userCreate -> System.out.println("Admin user already seeded"),
                () -> {
                    var userCreate = new User();
                    userCreate.setUsername(ADMIN_USERNAME);
                    userCreate.setEmail(ADMIN_EMAIL);
                    userCreate.setRoles(Set.of(roleAdmin));
                    userCreate.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
                    userCreate.setActive(true);
                    userCreate.setCreatedAt(Instant.now());

                    this.userRepository.save(userCreate);
                }
        );
    }
}

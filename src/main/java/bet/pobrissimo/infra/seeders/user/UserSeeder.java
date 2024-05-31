package bet.pobrissimo.infra.seeders.user;

import bet.pobrissimo.core.enums.RoleEnum;
import bet.pobrissimo.core.repository.RoleRepository;
import bet.pobrissimo.core.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserSeeder(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedUsers();
    }

    private void seedUsers() {
        System.out.println("Seeding users");
        var users = this.userRepository.findByEmail("teste1@teste.com");

        var rolePlayer = this.roleRepository.findByName(RoleEnum.PLAYER.getName());

        
    }
}

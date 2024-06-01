package bet.pobrissimo.infra.seeders.user;

import bet.pobrissimo.core.enums.RoleEnum;
import bet.pobrissimo.core.model.User;
import bet.pobrissimo.core.repository.RoleRepository;
import bet.pobrissimo.core.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Set;

@Configuration
@Order(1)
public class UserSeeder implements CommandLineRunner {

    @Value("${app.admin.username}")
    private String ADMIN_USERNAME;

    private final Faker faker = new Faker(Locale.of("pt-BR"));

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
        var userAdmin = this.userRepository.findByUsername(ADMIN_USERNAME);
        var rolePlayer = this.roleRepository.findByName(RoleEnum.PLAYER.getName());

        if (userAdmin.isPresent()) {
            System.out.println("Users already exist: " + userAdmin.get().getUsername());
            return;
        }

        for (int i = 0; i < 10; i++) {
            var user = new User();
            user.setUsername(faker.internet().username());
            user.setEmail(faker.internet().emailAddress());
            user.setPassword(passwordEncoder.encode(faker.internet().password()));
            user.setRoles(Set.of(rolePlayer));
            user.setActive(true);
            user.setCreatedAt(faker.date().birthday().toInstant());
            this.userRepository.save(user);
        }

        System.out.println("User seeding completed");
    }
}

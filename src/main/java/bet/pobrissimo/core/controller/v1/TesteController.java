package bet.pobrissimo.core.controller.v1;

import bet.pobrissimo.core.enums.RoleEnum;
import bet.pobrissimo.core.model.User;
import bet.pobrissimo.core.model.Wallet;
import bet.pobrissimo.core.repository.RoleRepository;
import bet.pobrissimo.core.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Set;

@RestController
@RequestMapping("/teste")
public class TesteController {

    private final Faker faker = new Faker(Locale.of("pt-BR"));

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public TesteController(UserRepository userRepository,
                      BCryptPasswordEncoder passwordEncoder,
                      RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Player')")
    public String teste() {
        return "Teste";
    }

    @GetMapping("/add-users/{number}")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<?> addUsers(@PathVariable("number") int number) {

        var rolePlayer = this.roleRepository.findByName(RoleEnum.PLAYER.getName());

        for (int i = 0; i < number; i++) {
            var user = new User();
            user.setUsername(faker.internet().username());
            user.setEmail(faker.internet().emailAddress());
            user.setPassword(passwordEncoder.encode(faker.internet().password()));
            user.setRoles(Set.of(rolePlayer));
            user.setActive(true);
            user.setCreatedAt(faker.date().birthday().toInstant());

            Wallet wallet = new Wallet();
            wallet.setUser(user);
            wallet.setAmount(BigDecimal.valueOf(faker.number().randomDouble(2, 0, 1000)));
            user.setWallet(wallet);

            this.userRepository.save(user);
        }
        return ResponseEntity.ok().build();
    }
}

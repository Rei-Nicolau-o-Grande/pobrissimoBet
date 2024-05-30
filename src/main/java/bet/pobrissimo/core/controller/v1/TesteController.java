package bet.pobrissimo.core.controller.v1;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class TesteController {

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Player')")
    public String teste() {
        return "Teste";
    }
}

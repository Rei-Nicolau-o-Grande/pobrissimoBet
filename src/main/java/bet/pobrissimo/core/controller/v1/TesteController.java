package bet.pobrissimo.core.controller.v1;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class TesteController {

    @GetMapping()
    @PreAuthorize("hasAuthority('SCOPE_Admin')")
    public String teste() {
        return "Teste";
    }
}

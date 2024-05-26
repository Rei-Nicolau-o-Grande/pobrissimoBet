package bet.pobrissimo.core.controller.v1;

import bet.pobrissimo.core.controller.BaseController;
import bet.pobrissimo.core.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController implements BaseController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<?> create(Object entity) {
        return null;
    }

    @Override
    public ResponseEntity<?> update(Object entity) {
        return null;
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> findAllPageable() {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(Object entity) {
        return null;
    }
}

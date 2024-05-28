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
    public ResponseEntity<?> create(Object entity) throws RuntimeException {
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public ResponseEntity<?> update(Object entity) throws RuntimeException {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public ResponseEntity<?> findById(Long id) throws RuntimeException {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public ResponseEntity<?> delete(Object entity) throws RuntimeException {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
}

package bet.pobrissimo.core.enums;

import bet.pobrissimo.core.model.Role;
import bet.pobrissimo.core.model.User;

public enum RoleEnum {
    ADMIN(1L, "Admin"),
    PLAYER(2L, "Player");

    private final Long id;
    private final String name;

    RoleEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role toRole() {
        return new Role(id, name);
    }

}

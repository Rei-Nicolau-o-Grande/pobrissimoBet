package bet.pobrissimo.enums;

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

}

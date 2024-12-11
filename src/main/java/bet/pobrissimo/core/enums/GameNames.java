package bet.pobrissimo.core.enums;

public enum GameNames {

    BURRINHO("Burrinho"),
    ROLETA_DA_PICANHA("Roleta da Picanha");

    private final String name;

    GameNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

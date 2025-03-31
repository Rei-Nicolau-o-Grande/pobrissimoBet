package bet.pobrissimo.enums;

public enum GameNames {

    BURRINHO_FORTUNE("Burrinho Fortune"),
    RODA_RODA_PICANHA("Roda Roda Picanha");

    private final String name;

    GameNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

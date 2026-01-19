package AG.LikeDislike.enums;

public enum PersonajeFuente {
    RICK_MORTY("rickmorty"),
    POKEMON("pokemon"),
    SUPERHERO("superhero");
    private final String value;

    PersonajeFuente(String value
    ) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PersonajeFuente fromValue(String value) {
        for (PersonajeFuente source : PersonajeFuente.values()) {
            if (source.value.equalsIgnoreCase(value)) {
                return source;
            }
        }
        throw new IllegalArgumentException("Unknown character source: " + value);
    }
}

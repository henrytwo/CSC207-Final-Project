package util;

import java.util.UUID;

public class Pair {
    UUID key;
    UUID value;

    Pair(UUID key, UUID value) {
        this.key = key;
        this.value = value;
    }

    public UUID getKey() {
        return key;
    }

    public UUID getValue() {
        return value;
    }
}

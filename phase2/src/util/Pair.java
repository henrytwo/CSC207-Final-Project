package util;

/**
 * Generic pair class
 *
 * @param <T> key type
 * @param <U> value type
 */
public class Pair<T, U> {
    private T key;
    private U value;

    /**
     * Creates a pair
     *
     * @param key
     * @param value
     */
    public Pair(T key, U value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Fetches key
     *
     * @return
     */
    public T getKey() {
        return key;
    }

    /**
     * Fetches value
     *
     * @return
     */
    public U getValue() {
        return value;
    }
}

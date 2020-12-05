package gateway;

import java.io.*;

/**
 * Serializes an object and writes it to a file. Loads the file into memory if possible, or initialize new object.
 *
 * @param <T> type of object being serialized
 */
public class Serializer<T> {
    String fileName;

    /**
     * Creates a serializer that serializes an object to a file name
     *
     * @param fileName name of serialized file
     */
    public Serializer(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Loads a serialized object from file. If that fails, then return the fall back object (which should be a new instance)
     *
     * @param fallback object to return if unable to read from file
     * @return object deserialized from file, or fallback if applicable
     */
    public T load(T fallback) {
        try {
            InputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            return (T) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return fallback;
        }
    }

    /**
     * Write the object to file
     *
     * @param object object to serialize
     */
    public void save(T object) {
        try {
            OutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(object);
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

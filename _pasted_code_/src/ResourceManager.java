/*
Name: Shesan Govindasamy
*/

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceManager {

    public static void save(Serializable data, String file) throws Exception {
        try (ObjectOutputStream outstream = new ObjectOutputStream(Files.newOutputStream(Paths.get(file)))) {
            outstream.writeObject(data);
        }
    }

    public static Object load(String file) throws Exception {
        try (ObjectInputStream instream = new ObjectInputStream(Files.newInputStream(Paths.get(file)))) {
            return instream.readObject();
        }
    }
}
package by.latushko.anyqueries.util.file;

import java.util.Optional;

public class FileHelper {
    private static final String FILE_EXTENSION_DELIMITER = ".";
    public static Optional<String> getExtension(String fileName) {
        if(fileName != null && !fileName.isEmpty() && fileName.contains(FILE_EXTENSION_DELIMITER)) {
            return Optional.of(fileName.substring(fileName.lastIndexOf(FILE_EXTENSION_DELIMITER)));
        } else {
            return Optional.empty();
        }
    }
}

package photos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Utility class for managing photos.
 * Provides methods for loading photos from the filesystem and getting photo metadata.
 * 
 * @author Group XX
 */
public class PhotoManager {

    /**
     * Creates a Photo object from a file path.
     * The photo's date is set to the file's last modification time.
     *
     * @param filePath the path to the photo file
     * @return a Photo object, or null if the file doesn't exist or an error occurs
     */
    public static Photo createPhotoFromFile(String filePath) {
        try {
            if (!Files.exists(Paths.get(filePath))) {
                return null;
            }

            long lastModified = Files.getLastModifiedTime(Paths.get(filePath)).toMillis();
            LocalDateTime date = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(lastModified),
                ZoneId.systemDefault()
            );

            return new Photo(filePath, date);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Checks if a file is a valid photo format.
     * Supported formats: BMP, GIF, JPEG, JPG, PNG
     *
     * @param filePath the path to the file
     * @return true if the file is a valid photo format, false otherwise
     */
    public static boolean isValidPhotoFormat(String filePath) {
        String lowerPath = filePath.toLowerCase();
        return lowerPath.endsWith(".bmp") || 
               lowerPath.endsWith(".gif") || 
               lowerPath.endsWith(".jpeg") || 
               lowerPath.endsWith(".jpg") || 
               lowerPath.endsWith(".png");
    }

    /**
     * Gets the file extension of a photo.
     *
     * @param filePath the path to the photo file
     * @return the file extension (e.g., "jpg"), or empty string if none
     */
    public static String getFileExtension(String filePath) {
        int lastDot = filePath.lastIndexOf('.');
        if (lastDot > 0 && lastDot < filePath.length() - 1) {
            return filePath.substring(lastDot + 1).toLowerCase();
        }
        return "";
    }

    /**
     * Gets the file name from a file path.
     *
     * @param filePath the path to the file
     * @return the file name (without directory path)
     */
    public static String getFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }
}

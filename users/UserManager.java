package users;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Manages user accounts and persistence.
 * Handles user creation, deletion, retrieval, and serialization to disk.
 * 
 * @author Group XX
 */
public class UserManager {
    private static final String USERS_DIR = "data/users";
    private static final String USER_FILE_EXTENSION = ".dat";
    
    /**
     * Gets the file path for a user's data file.
     *
     * @param username the username
     * @return the file path
     */
    private static String getUserFilePath(String username) {
        return USERS_DIR + "/" + username + USER_FILE_EXTENSION;
    }

    /**
     * Loads a user from disk.
     *
     * @param username the username to load
     * @return the User object, or null if not found
     * @throws IOException if there's an error reading the file
     * @throws ClassNotFoundException if the User class cannot be found
     */
    public static User loadUser(String username) throws IOException, ClassNotFoundException {
        String filePath = getUserFilePath(username);
        if (!Files.exists(Paths.get(filePath))) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (User) ois.readObject();
        }
    }

    /**
     * Saves a user to disk.
     *
     * @param user the user to save
     * @throws IOException if there's an error writing the file
     */
    public static void saveUser(User user) throws IOException {
        // Ensure the users directory exists
        Files.createDirectories(Paths.get(USERS_DIR));

        String filePath = getUserFilePath(user.getUsername());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(user);
            oos.flush();
        }
    }

    /**
     * Deletes a user from disk.
     *
     * @param username the username to delete
     * @return true if the user was deleted, false if not found
     * @throws IOException if there's an error deleting the file
     */
    public static boolean deleteUser(String username) throws IOException {
        String filePath = getUserFilePath(username);
        if (Files.exists(Paths.get(filePath))) {
            Files.delete(Paths.get(filePath));
            return true;
        }
        return false;
    }

    /**
     * Checks if a user exists.
     *
     * @param username the username to check
     * @return true if the user exists, false otherwise
     */
    public static boolean userExists(String username) {
        String filePath = getUserFilePath(username);
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Gets all usernames in the system.
     *
     * @return a list of all usernames
     * @throws IOException if there's an error reading the directory
     */
    public static List<String> getAllUsers() throws IOException {
        List<String> usernames = new ArrayList<>();
        
        if (!Files.exists(Paths.get(USERS_DIR))) {
            return usernames;
        }

        Files.list(Paths.get(USERS_DIR))
            .filter(path -> path.toString().endsWith(USER_FILE_EXTENSION))
            .forEach(path -> {
                String filename = path.getFileName().toString();
                String username = filename.substring(0, filename.length() - USER_FILE_EXTENSION.length());
                usernames.add(username);
            });

        return usernames;
    }

    /**
     * Authenticates a user by checking username and password.
     * For the admin user, password should be "admin".
     * For the stock user, password should be "stock" or empty.
     *
     * @param username the username
     * @param password the password
     * @return the User object if authentication succeeds, null otherwise
     */
    public static User authenticate(String username, String password) {
        try {
            if (!userExists(username)) {
                return null;
            }

            User user = loadUser(username);
            if (user == null) {
                return null;
            }

            // Check password
            String expectedPassword = user.getPassword();
            if (password == null) {
                password = "";
            }

            if (expectedPassword.equals(password)) {
                return user;
            }

            return null;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Creates a new user.
     * Also initializes the stock user with the stock album if it doesn't exist.
     *
     * @param username the username for the new user
     * @param password the password for the new user
     * @return the new User object
     * @throws IOException if there's an error saving the user
     */
    public static User createUser(String username, String password) throws IOException {
        User user = new User(username, password);
        saveUser(user);
        return user;
    }

    /**
     * Initializes default users (admin and stock) if they don't exist.
     * This should be called once during application startup.
     *
     * @throws IOException if there's an error
     */
    public static void initializeDefaultUsers() throws IOException {
        // Initialize admin user if needed
        if (!userExists("admin")) {
            Admin admin = new Admin();
            saveUser(admin);
        }
        
        // Initialize stock user if needed
        if (!userExists("stock")) {
            Stock stockUser = new Stock();
            stockUser.createAlbum("stock");
            saveUser(stockUser);
        }
    }
    
    /**
     * Initializes the stock user with stock photos.
     * This should be called once during application startup.
     *
     * @throws IOException if there's an error
     * @deprecated Use initializeDefaultUsers() instead
     */
    @Deprecated
    public static void initializeStockUserIfNeeded() throws IOException {
        initializeDefaultUsers();
    }
}

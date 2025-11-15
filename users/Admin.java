package users;

/**
 * Represents the admin user of the photo application.
 * The admin user can manage other users (create, delete, list).
 * The admin cannot manage albums or photos.
 * Default password is "admin".
 * 
 * @author Group XX
 */
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an Admin user with the username "admin" and password "admin".
     */
    public Admin() {
        super("admin", "admin");
    }
}

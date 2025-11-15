package users;

/**
 * Represents the stock user of the photo application.
 * The stock user contains pre-loaded stock photos in a "stock" album.
 * Default password is "stock".
 * 
 * @author Group XX
 */
public class Stock extends User {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a Stock user with the username "stock" and password "stock".
     */
    public Stock() {
        super("stock", "stock");
    }
}

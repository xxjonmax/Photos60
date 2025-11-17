package users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import photos.Album;

/**
 * Represents a user of the photo application.
 * A user has a username, password, and a collection of albums.
 * 
 * @author Group 60
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private final List<Album> albums;

    /**
     * Constructs a User with the specified username and password.
     *
     * @param username the username
     * @param password the password (can be empty or null)
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password == null ? "" : password;
        this.albums = new ArrayList<>();
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password == null ? "" : password;
    }

    /**
     * Gets all albums for this user.
     *
     * @return a list of albums
     */
    public List<Album> getAlbums() {
        return new ArrayList<>(albums);
    }

    /**
     * Gets the number of albums for this user.
     *
     * @return the album count
     */
    public int getAlbumCount() {
        return albums.size();
    }

    /**
     * Finds an album by name.
     *
     * @param albumName the name of the album
     * @return the album, or null if not found
     */
    public Album getAlbum(String albumName) {
        for (Album album : albums) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }
        return null;
    }

    /**
     * Creates a new album.
     * Album names must be unique for a user.
     *
     * @param albumName the name of the new album
     * @return true if the album was created, false if an album with that name already exists
     */
    public boolean createAlbum(String albumName) {
        if (getAlbum(albumName) != null) {
            return false;
        }
        albums.add(new Album(albumName));
        return true;
    }

    /**
     * Deletes an album.
     *
     * @param albumName the name of the album to delete
     * @return true if the album was deleted, false if it doesn't exist
     */
    public boolean deleteAlbum(String albumName) {
        Album album = getAlbum(albumName);
        if (album != null) {
            return albums.remove(album);
        }
        return false;
    }

    /**
     * Renames an album.
     *
     * @param oldName the current album name
     * @param newName the new album name
     * @return true if the album was renamed, false if the old album doesn't exist
     *         or an album with the new name already exists
     */
    public boolean renameAlbum(String oldName, String newName) {
        Album album = getAlbum(oldName);
        if (album == null || getAlbum(newName) != null) {
            return false;
        }
        album.setName(newName);
        return true;
    }

    /**
     * Compares this user with another object.
     * Two users are equal if they have the same username.
     *
     * @param obj the object to compare
     * @return true if the users are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return username.equals(user.username);
    }

    /**
     * Returns a hash code for this user.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }

    /**
     * Returns a string representation of this user.
     *
     * @return a string containing the username and album count
     */
    @Override
    public String toString() {
        return username + " (" + albums.size() + " albums)";
    }
}

package users;

import photos.Photo;

/**
 * Interface for managing photos in albums.
 * Provides operations for adding, removing, copying, and moving photos.
 * 
 * @author Group 60
 */
public interface ManagePhotos {

    /**
     * Adds a photo to an album.
     *
     * @param albumName the name of the album
     * @param photo the photo to add
     * @return true if the photo was added, false if it's a duplicate
     */
    boolean addPhotoToAlbum(String albumName, Photo photo);

    /**
     * Removes a photo from an album.
     *
     * @param albumName the name of the album
     * @param photo the photo to remove
     * @return true if the photo was removed, false if it wasn't in the album
     */
    boolean removePhotoFromAlbum(String albumName, Photo photo);

    /**
     * Copies a photo from one album to another.
     *
     * @param sourceAlbum the name of the source album
     * @param destAlbum the name of the destination album
     * @param photo the photo to copy
     * @return true if the photo was copied, false otherwise
     */
    boolean copyPhotoToAlbum(String sourceAlbum, String destAlbum, Photo photo);

    /**
     * Moves a photo from one album to another.
     * The photo is removed from the source album and added to the destination album.
     *
     * @param sourceAlbum the name of the source album
     * @param destAlbum the name of the destination album
     * @param photo the photo to move
     * @return true if the photo was moved, false otherwise
     */
    boolean movePhotoToAlbum(String sourceAlbum, String destAlbum, Photo photo);
}

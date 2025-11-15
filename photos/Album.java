package photos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an album that contains a collection of photos.
 * An album has a name and a list of photos.
 * 
 * @author Group XX
 */
public class Album implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private List<Photo> photos;

    /**
     * Constructs an Album with the specified name.
     *
     * @param name the name of the album
     */
    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<>();
    }

    /**
     * Gets the name of the album.
     *
     * @return the album name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the album.
     *
     * @param name the new album name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets all photos in this album.
     *
     * @return a list of photos
     */
    public List<Photo> getPhotos() {
        return new ArrayList<>(photos);
    }

    /**
     * Gets the number of photos in this album.
     *
     * @return the photo count
     */
    public int getPhotoCount() {
        return photos.size();
    }

    /**
     * Adds a photo to this album.
     * Duplicate photos (same file path) are not added.
     *
     * @param photo the photo to add
     * @return true if the photo was added, false if it was a duplicate
     */
    public boolean addPhoto(Photo photo) {
        if (photos.contains(photo)) {
            return false;
        }
        photos.add(photo);
        return true;
    }

    /**
     * Removes a photo from this album.
     *
     * @param photo the photo to remove
     * @return true if the photo was removed, false if it didn't exist
     */
    public boolean removePhoto(Photo photo) {
        return photos.remove(photo);
    }

    /**
     * Checks if this album contains a specific photo.
     *
     * @param photo the photo to check
     * @return true if the photo is in the album, false otherwise
     */
    public boolean containsPhoto(Photo photo) {
        return photos.contains(photo);
    }

    /**
     * Gets the earliest date among all photos in the album.
     *
     * @return the earliest date, or null if the album has no photos
     */
    public LocalDateTime getEarliestDate() {
        if (photos.isEmpty()) {
            return null;
        }
        LocalDateTime earliest = photos.get(0).getDate();
        for (Photo photo : photos) {
            if (photo.getDate().isBefore(earliest)) {
                earliest = photo.getDate();
            }
        }
        return earliest;
    }

    /**
     * Gets the latest date among all photos in the album.
     *
     * @return the latest date, or null if the album has no photos
     */
    public LocalDateTime getLatestDate() {
        if (photos.isEmpty()) {
            return null;
        }
        LocalDateTime latest = photos.get(0).getDate();
        for (Photo photo : photos) {
            if (photo.getDate().isAfter(latest)) {
                latest = photo.getDate();
            }
        }
        return latest;
    }

    /**
     * Gets a photo by index.
     *
     * @param index the index of the photo
     * @return the photo at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Photo getPhotoAt(int index) {
        return photos.get(index);
    }

    /**
     * Returns a string representation of this album.
     *
     * @return a string containing the album name and photo count
     */
    @Override
    public String toString() {
        return name + " (" + photos.size() + " photos)";
    }
}

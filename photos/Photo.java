package photos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a photo in the application.
 * A photo has a file path, date taken, caption, and list of tags.
 * The date is derived from the file's last modification time.
 * 
 * @author Group 60
 */
public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String filePath;
    private LocalDateTime date;
    private String caption;
    private List<Tag> tags;

    /**
     * Constructs a Photo with the specified file path and date.
     *
     * @param filePath the absolute path to the photo file
     * @param date the date/time the photo was taken (from file modification time)
     */
    public Photo(String filePath, LocalDateTime date) {
        this.filePath = filePath;
        this.date = date;
        this.caption = "";
        this.tags = new ArrayList<>();
    }

    /**
     * Gets the file path of the photo.
     *
     * @return the file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the file path of the photo.
     *
     * @param filePath the file path
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the date the photo was taken.
     *
     * @return the date/time
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets the date the photo was taken.
     *
     * @param date the date/time
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Gets the caption of the photo.
     *
     * @return the caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Sets the caption of the photo.
     *
     * @param caption the caption
     */
    public void setCaption(String caption) {
        this.caption = caption == null ? "" : caption;
    }

    /**
     * Gets all tags for this photo.
     *
     * @return a list of tags
     */
    public List<Tag> getTags() {
        return new ArrayList<>(tags);
    }

    /**
     * Adds a tag to this photo.
     * Duplicate tags (same type and value) are not added.
     *
     * @param tag the tag to add
     * @return true if the tag was added, false if it was a duplicate
     */
    public boolean addTag(Tag tag) {
        if (tags.contains(tag)) {
            return false;
        }
        tags.add(tag);
        return true;
    }

    /**
     * Removes a tag from this photo.
     *
     * @param tag the tag to remove
     * @return true if the tag was removed, false if it didn't exist
     */
    public boolean removeTag(Tag tag) {
        return tags.remove(tag);
    }

    /**
     * Removes all tags of a specific type.
     *
     * @param type the tag type to remove
     */
    public void removeTagsByType(String type) {
        tags.removeIf(tag -> tag.getType().equals(type));
    }

    /**
     * Finds all tags of a specific type.
     *
     * @param type the tag type to find
     * @return a list of tags with the specified type
     */
    public List<Tag> getTagsByType(String type) {
        List<Tag> result = new ArrayList<>();
        for (Tag tag : tags) {
            if (tag.getType().equals(type)) {
                result.add(tag);
            }
        }
        return result;
    }

    /**
     * Checks if this photo has a specific tag.
     *
     * @param tag the tag to check
     * @return true if the tag exists, false otherwise
     */
    public boolean hasTag(Tag tag) {
        return tags.contains(tag);
    }

    /**
     * Compares this photo with another object.
     * Two photos are equal if they have the same file path.
     *
     * @param obj the object to compare
     * @return true if the photos are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Photo photo = (Photo) obj;
        return filePath.equals(photo.filePath);
    }

    /**
     * Returns a hash code for this photo.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return filePath.hashCode();
    }

    /**
     * Gets the file name from the file path.
     *
     * @return the file name without directory path
     */
    public String getFileName() {
        int lastSlash = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        if (lastSlash == -1) {
            return filePath;
        }
        return filePath.substring(lastSlash + 1);
    }

    /**
     * Returns a string representation of this photo.
     *
     * @return a string containing the file path and caption
     */
    @Override
    public String toString() {
        return filePath + " [" + caption + "]";
    }
}
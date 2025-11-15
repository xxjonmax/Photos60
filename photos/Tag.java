package photos;

import java.io.Serializable;

/**
 * Represents a tag that can be applied to a photo.
 * A tag consists of a type (e.g., "location", "person") and a value (e.g., "Prague", "Alice").
 * 
 * @author Group XX
 */
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String type;
    private String value;

    /**
     * Constructs a Tag with the specified type and value.
     *
     * @param type the tag type (e.g., "location", "person")
     * @param value the tag value (e.g., "Prague", "Alice")
     */
    public Tag(String type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Gets the tag type.
     *
     * @return the tag type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the tag type.
     *
     * @param type the tag type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the tag value.
     *
     * @return the tag value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the tag value.
     *
     * @param value the tag value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Compares this tag with another object.
     * Two tags are equal if they have the same type and value.
     *
     * @param obj the object to compare
     * @return true if the tags are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tag tag = (Tag) obj;
        return type.equals(tag.type) && value.equals(tag.value);
    }

    /**
     * Returns a hash code for this tag.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return (type + value).hashCode();
    }

    /**
     * Returns a string representation of this tag.
     *
     * @return a string in the format "type:value"
     */
    @Override
    public String toString() {
        return type + ":" + value;
    }
}

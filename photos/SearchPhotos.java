package photos;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Utility class for searching photos based on various criteria.
 * Supports date range searches, tag-based searches, and conjunctions/disjunctions.
 * 
 * @author Group XX
 */
public class SearchPhotos {

    /**
     * Searches for photos within a date range.
     *
     * @param photos the list of photos to search
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return a list of photos within the date range
     */
    public static List<Photo> searchByDateRange(List<Photo> photos, LocalDateTime startDate, LocalDateTime endDate) {
        List<Photo> results = new ArrayList<>();
        for (Photo photo : photos) {
            LocalDateTime photoDate = photo.getDate();
            if (!photoDate.isBefore(startDate) && !photoDate.isAfter(endDate)) {
                results.add(photo);
            }
        }
        return results;
    }

    /**
     * Searches for photos with a specific tag.
     *
     * @param photos the list of photos to search
     * @param tagType the tag type
     * @param tagValue the tag value
     * @return a list of photos with the specified tag
     */
    public static List<Photo> searchByTag(List<Photo> photos, String tagType, String tagValue) {
        List<Photo> results = new ArrayList<>();
        Tag searchTag = new Tag(tagType, tagValue);
        for (Photo photo : photos) {
            if (photo.hasTag(searchTag)) {
                results.add(photo);
            }
        }
        return results;
    }

    /**
     * Searches for photos with multiple tags using AND logic.
     * A photo must have all specified tags to be included.
     *
     * @param photos the list of photos to search
     * @param tags the list of tags (tag type-value pairs)
     * @return a list of photos with all specified tags
     */
    public static List<Photo> searchByTagsAnd(List<Photo> photos, List<Tag> tags) {
        List<Photo> results = new ArrayList<>();
        for (Photo photo : photos) {
            boolean hasAllTags = true;
            for (Tag tag : tags) {
                if (!photo.hasTag(tag)) {
                    hasAllTags = false;
                    break;
                }
            }
            if (hasAllTags) {
                results.add(photo);
            }
        }
        return results;
    }

    /**
     * Searches for photos with multiple tags using OR logic.
     * A photo must have at least one of the specified tags to be included.
     *
     * @param photos the list of photos to search
     * @param tags the list of tags (tag type-value pairs)
     * @return a list of photos with at least one of the specified tags
     */
    public static List<Photo> searchByTagsOr(List<Photo> photos, List<Tag> tags) {
        List<Photo> results = new ArrayList<>();
        for (Photo photo : photos) {
            for (Tag tag : tags) {
                if (photo.hasTag(tag)) {
                    results.add(photo);
                    break;
                }
            }
        }
        return results;
    }

    /**
     * Searches for photos with AND logic using two tag criteria.
     *
     * @param photos the list of photos to search
     * @param tag1Type the first tag type
     * @param tag1Value the first tag value
     * @param tag2Type the second tag type
     * @param tag2Value the second tag value
     * @return a list of photos with both tags
     */
    public static List<Photo> searchByTwoTagsAnd(List<Photo> photos, String tag1Type, String tag1Value, String tag2Type, String tag2Value) {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(tag1Type, tag1Value));
        tags.add(new Tag(tag2Type, tag2Value));
        return searchByTagsAnd(photos, tags);
    }

    /**
     * Searches for photos with OR logic using two tag criteria.
     *
     * @param photos the list of photos to search
     * @param tag1Type the first tag type
     * @param tag1Value the first tag value
     * @param tag2Type the second tag type
     * @param tag2Value the second tag value
     * @return a list of photos with at least one of the tags
     */
    public static List<Photo> searchByTwoTagsOr(List<Photo> photos, String tag1Type, String tag1Value, String tag2Type, String tag2Value) {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(tag1Type, tag1Value));
        tags.add(new Tag(tag2Type, tag2Value));
        return searchByTagsOr(photos, tags);
    }

    /**
     * Gets all unique tag types from a list of photos.
     *
     * @param photos the list of photos
     * @return a sorted list of unique tag types
     */
    public static List<String> getAllTagTypes(List<Photo> photos) {
        Set<String> tagTypes = new HashSet<>();
        for (Photo photo : photos) {
            for (Tag tag : photo.getTags()) {
                tagTypes.add(tag.getType());
            }
        }
        List<String> result = new ArrayList<>(tagTypes);
        Collections.sort(result);
        return result;
    }

    /**
     * Gets all unique values for a specific tag type.
     *
     * @param photos the list of photos
     * @param tagType the tag type
     * @return a sorted list of unique values for that tag type
     */
    public static List<String> getTagValues(List<Photo> photos, String tagType) {
        Set<String> values = new HashSet<>();
        for (Photo photo : photos) {
            for (Tag tag : photo.getTags()) {
                if (tag.getType().equals(tagType)) {
                    values.add(tag.getValue());
                }
            }
        }
        List<String> result = new ArrayList<>(values);
        Collections.sort(result);
        return result;
    }
}

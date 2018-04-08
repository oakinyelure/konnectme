package me.konnect;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Event {
    private String id;

    @SerializedName("event_name")
    private String name;

    private String description;
    private List<String> organizers;
    private String location;
    private List<String> tags;
    private List<String> interestedUsers;
    // createdTimestamp in milliseconds
    private long createdTimestamp;
    private long modifiedTimestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getOrganizers() {
        return organizers;
    }

    public void setOrganizers(List<String> organizers) {
        this.organizers = organizers;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getInterestedUsers() {
        return interestedUsers;
    }

    public void setInterestedUsers(List<String> interestedUsers) {
        this.interestedUsers = interestedUsers;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long timestamp) {
        this.createdTimestamp = timestamp;
    }

    public long getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(long modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("description", description)
                .add("organizers", organizers)
                .add("location", location)
                .add("tags", tags)
                .add("interested_users", interestedUsers)
                .add("created", createdTimestamp)
                .add("modified", modifiedTimestamp)
                .toString();
    }
}

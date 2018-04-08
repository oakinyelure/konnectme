package me.konnect;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Event {
    private String id;

    @SerializedName("event_name")
    private String name;

    private List<String> organizers;
    private String location;
    private List<String> tags;

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

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("organizers", organizers)
                .add("location", location)
                .add("tags", tags)
                .toString();
    }
}

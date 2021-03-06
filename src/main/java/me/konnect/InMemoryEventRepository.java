package me.konnect;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryEventRepository implements EventRepository {
    @Override
    public Optional<Event> getEventById(@Nonnull String eventId) {
        Event event = new Event();
        event.setId("1");
        event.setName("Indoor soccer");
        event.setLocation("TSU recreational complex");

        List<String> organizers = new ArrayList<>();
        organizers.add("anthonypowell");
        organizers.add("jamaal");
        organizers.add("francelldavidson");

        event.setOrganizers(organizers);

        List<String> tags = new ArrayList<>();
        tags.add("soccer");
        tags.add("sports");
        tags.add("football");
        event.setTags(tags);

//        return Optional.of(event);
        return Optional.empty();
    }

    @Override
    public List<Event> getEventByTag(@Nonnull String tag, int limit) {
        return null;
    }

    @Override
    public List<Event> getEventByTagAfterTime(@Nonnull String tag, long timestamp, int limit) {
        return null;
    }

    @Override
    public List<Event> getLatestEvents(int limit) {
        return null;
    }

    @Override
    public List<Event> getEventAfterTimestamp(long timestamp, int limit) {
        return null;
    }

    @Override
    public Event addEvent(@Nonnull Event event) {
        return null;
    }

    @Override
    public void deleteEvent(@Nonnull String eventId) {

    }
}

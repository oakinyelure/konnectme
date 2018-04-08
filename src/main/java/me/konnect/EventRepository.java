package me.konnect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Optional<Event> getEventById(@Nonnull String eventId);

    List<Event> getEventByTag(@Nonnull String tag, int limit);

    List<Event> getEventByTagAfterTime(@Nonnull String tag, long timestamp, int limit);

    List<Event> getLatestEvents(int limit);

    List<Event> getEventAfterTimestamp(long timestamp, int limit);

    Event addEvent(@Nonnull Event event);

    void deleteEvent(@Nonnull String eventId);
}

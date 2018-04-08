package me.konnect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Optional<Event> getEventById(@Nonnull String eventId);

    List<Event> getEventByTag(@Nonnull String tag);
}

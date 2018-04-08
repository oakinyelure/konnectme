package me.konnect;

import com.google.cloud.datastore.*;
import com.google.datastore.v1.CompositeFilter;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
TODO: Look into using projection queries
 */
public class GcloudEventRepository implements EventRepository {
    private static final String KIND = "Events";
    private Datastore datastore;
    private KeyFactory keyFactory;

    public GcloudEventRepository() {
        datastore = DatastoreOptions.getDefaultInstance().getService();
        keyFactory = datastore.newKeyFactory().setKind(KIND);
    }

    @Override
    public Optional<Event> getEventById(@Nonnull String eventId) {
        if(!validUrlSafeKey(eventId)) {
            return Optional.empty();
        }
        Entity entity = datastore.get(Key.fromUrlSafe(eventId));
        Optional<Event> result = entity == null ? Optional.empty() :
                                            Optional.of(fromEntity(entity));
        return result;
    }

    private boolean validUrlSafeKey(String urlKey) {
        try {
            Key.fromUrlSafe(urlKey);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Event> getEventByTag(@Nonnull String tag, int limit) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                                .setKind(KIND)
                                .setFilter(StructuredQuery.PropertyFilter.eq("tags", tag))
                                .setOrderBy(StructuredQuery.OrderBy.desc("created"))
                                .setLimit(limit)
                                .build();

        QueryResults<Entity> entities = datastore.run(query);
        List<Event> events = new ArrayList<>();
        while(entities.hasNext()) {
            Entity entity = entities.next();
            events.add(fromEntity(entity));
        }
        return events;
    }

    @Override
    public List<Event> getEventByTagAfterTime(@Nonnull String tag, long timestamp, int limit) {
        StructuredQuery.CompositeFilter filter = StructuredQuery.CompositeFilter.and(
                StructuredQuery.PropertyFilter.eq("tags", tag),
                StructuredQuery.PropertyFilter.lt("timestamp", timestamp));

        Query<Entity> query = Query.newEntityQueryBuilder()
                                .setKind(KIND)
                                .setFilter(filter)
                                .setOrderBy(StructuredQuery.OrderBy.desc("created"))
                                .setLimit(limit)
                                .build();

        QueryResults<Entity> entities = datastore.run(query);
        List<Event> events = new ArrayList<>();
        while(entities.hasNext()) {
            Entity entity = entities.next();
            events.add(fromEntity(entity));
        }
        return events;
    }

    @Override
    public List<Event> getLatestEvents(int limit) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                                .setKind(KIND)
                                .setOrderBy(StructuredQuery.OrderBy.desc("created"))
                                .setLimit(limit)
                                .build();

        QueryResults<Entity> entities = datastore.run(query);
        List<Event> events = new ArrayList<>();
        while(entities.hasNext()) {
            Entity entity = entities.next();
            events.add(fromEntity(entity));
        }
        return events;
    }

    @Override
    public List<Event> getEventAfterTimestamp(long timestamp, int limit) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                                .setKind(KIND)
                                .setFilter(StructuredQuery.PropertyFilter.lt("created", timestamp))
                                .setOrderBy(StructuredQuery.OrderBy.desc("created"))
                                .setLimit(limit)
                                .build();

        QueryResults<Entity> entities = datastore.run(query);
        List<Event> events = new ArrayList<>();
        while(entities.hasNext()) {
            Entity entity = entities.next();
            events.add(fromEntity(entity));
        }
        return events;
    }

    @Override
    public Event addEvent(@Nonnull Event event) {
        Key key = datastore.allocateId(keyFactory.newKey());
        Instant instant = Instant.now();
        long timestamp = instant.toEpochMilli();

        event.setId(key.toUrlSafe());
        event.setCreatedTimestamp(timestamp);
        event.setModifiedTimestamp(timestamp);

        Entity eventEntity = Entity.newBuilder(key)
                                    .set("name", event.getName())
                                    .set("organizers", toGcloudValueList(event.getOrganizers()))
                                    .set("location", event.getLocation())
                                    .set("description", event.getDescription())
                                    .set("tags", toGcloudValueList(event.getTags()))
                                    .set("created", event.getCreatedTimestamp())
                                    .set("modified", event.getModifiedTimestamp())
                                    .set("interested_users",
                                            toGcloudValueList(event.getInterestedUsers()))
                                    .build();

        datastore.put(eventEntity);
        return event;
    }

    @Override
    public void deleteEvent(@Nonnull String eventId) {
        datastore.delete(Key.fromUrlSafe(eventId));
    }

    private List<Value<String>> toGcloudValueList(List<String> list) {
        List<Value<String>> result = new ArrayList<>();
        for (String s : list) {
            result.add(StringValue.of(s));
        }
        return result;
    }

    private List<String> fromGcloudValueList(List<Value<String>> list) {
        List<String> result = new ArrayList<>();
        for (Value<String> s : list) {
            result.add(s.toString());
        }
        return result;
    }

    private Event fromEntity(Entity entity) {
        Event event = new Event();

        event.setId(entity.getKey().toUrlSafe());
        event.setName(entity.getString("name"));
        event.setLocation(entity.getString("location"));
        event.setOrganizers(fromGcloudValueList(entity.getList("organizers")));
        event.setDescription(entity.getString("description"));
        event.setTags(fromGcloudValueList(entity.getList("tags")));
        event.setCreatedTimestamp(entity.getLong("created"));
        event.setModifiedTimestamp(entity.getLong("modified"));
        event.setInterestedUsers(fromGcloudValueList(entity.getList("interested_users")));

        return event;
    }
}

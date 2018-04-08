package me.konnect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import java.util.Optional;

import static spark.Spark.exception;
import static spark.Spark.get;


public class FeedController {
    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

    @Inject
    private EventRepository eventRepository = new InMemoryEventRepository();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public FeedController() {
        initializeRoutes();
    }

    private void initializeRoutes() {
        filterById();
        filterByTags();
        handleExceptions();
    }

    private void filterByTags() {
        get("/event/tags", "application/json", (request, response) -> {

            return null;
        });
    }

    private void filterById() {
        get("/event/:id", "application/json", (request, response) -> {
            String id = request.params(":id");
            if(id == null) {
                throw new EventFeedException("No id was supplied");
            }

            logger.info("Request for event with id {}", id);

            Optional<Event> result = eventRepository.getEventById(id);
            if(!result.isPresent()) {
                throw new EventFeedException("Event with id " + id + " does not exist");
            }

            Event event = result.get();
            logger.info("Retrieved event: {}", event);
            response.status(200);
            response.header("Content-Type", "application/json");
            return gson.toJson(event);
        });
    }

    private void handleExceptions() {
        exception(EventFeedException.class, (e, request, response) -> {
            logger.error(e.getMessage());

            response.status(404);
            response.header("Content-Type", "application/json");

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error_msg", e.getMessage());
            response.body(gson.toJson(jsonObject));
        });
    }
}

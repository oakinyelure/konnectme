package me.konnect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import javax.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static spark.Spark.*;
import static spark.route.HttpMethod.post;


public class RouteController {
    private static final Logger logger = LoggerFactory.getLogger(RouteController.class);
    private static final int DEFAULT_LIMIT = 30;

    @Inject
    private EventRepository eventRepository = new GcloudEventRepository();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Configuration cfg;
    private FreeMarkerEngine freeMarkerEngine;

    public RouteController() {
        cfg = new Configuration(Configuration.VERSION_2_3_26);
        cfg.setDefaultEncoding("UTF-8");
//        cfg.setTemplateLoader(new ClassTemplateLoader(RouteController.class, "/public"));
        cfg.setClassForTemplateLoading(RouteController.class, "/public");
        freeMarkerEngine = new FreeMarkerEngine(cfg);

        initializeRoutes();
    }

    private void initializeRoutes() {
        getWelcomePage();
        getCreateEventPage();
        getFeed();
        createEvent();
        filterById();
        filterByTags();
        filterByLatest();
        handleExceptions();
    }

    private void getWelcomePage() {
        get("/", (request, response) -> {
            response.status(200);
            response.header("Content-Type", "text/html");
            return new ModelAndView(new HashMap<>(), "index.ftl");
        }, freeMarkerEngine);
    }

    private void getCreateEventPage() {
        get("/create", (request, response) -> {
            response.status(200);
            response.header("Content-Type", "text/html");
            return new ModelAndView(new HashMap<>(), "create.ftl");
        }, freeMarkerEngine);
    }

    private void getFeed() {
        get("/feed", (request, response) -> {
            response.status(200);
            response.header("Content-Type", "text/html");
            return new ModelAndView(new HashMap<>(), "feed.ftl");
        }, freeMarkerEngine);
    }

    private void createEvent() {
        post("/create", "application/json", (request, response) -> {
            Event event = gson.fromJson(request.body(), Event.class);
            logger.info("New proposed event\n{}", event);

            event = eventRepository.addEvent(event);
            response.status(HttpStatus.CREATED);
            return gson.toJson(event);

        });
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

    private void filterByLatest() {
        get("/events",  "application/json", (request, response) -> {
            int numberOfEvents = DEFAULT_LIMIT;
            String limit = request.queryParams("limit");
            String timeStamp = request.queryParams("after");

            if(limit != null) { // TODO: need to check if its numeric also
                numberOfEvents = Integer.parseInt(limit);
            }

            List<Event> events = null;
            if(timeStamp == null) {
                logger.info("Retrieving the first {} latest events", limit);
                events = eventRepository.getLatestEvents(numberOfEvents);
            } else {
                logger.info("Retrieving the first {} events that occurred after {}",
                                limit, timeStamp);
                events = eventRepository.getEventAfterTimestamp(Long.parseLong(timeStamp),
                                                                numberOfEvents);
            }
            response.status(200);
            response.header("Content-Type", "application/json");

            return gson.toJson(events);
        });
    }

    private void handleExceptions() {
        exception(EventFeedException.class, (e, request, response) -> {
            logger.error(e.getMessage());

            response.status(e.getHttpErrorCode());
            response.header("Content-Type", "application/json");

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", e.getMessage());
            response.body(gson.toJson(jsonObject));
        });
    }
}

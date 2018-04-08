package me.konnect;

import freemarker.template.Configuration;
import org.apache.commons.cli.*;
import spark.Spark;

import static spark.Spark.get;

public class ServiceRunner {
    private static int defaultPort = 8088;

    public static void main(String[] args) throws ParseException {
        CommandLine cmd = generateCommandLine(generateOptions(), args);

        int portNumber = defaultPort;
        if(cmd.hasOption("p")) {
            portNumber = Integer.parseInt(cmd.getOptionValue("p"));
        }
        Spark.port(portNumber);
        Spark.staticFileLocation("/public");

        new RouteController();
    }

    public static Options generateOptions() {
        final Option portOption = Option.builder("p")
                                        .required()
                                        .hasArg()
                                        .longOpt("port")
                                        .desc("Port number to run service")
                                        .build();

        final Options options = new Options();
        options.addOption(portOption);
        return options;
    }

    public static CommandLine generateCommandLine(final Options options, final String[] args) throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }
}

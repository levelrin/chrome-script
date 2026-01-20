package com.levelrin.chromescript;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {

    public static void main(final String... args) throws ParseException {
        final Options options = new Options();
        options.addOption("h", "help", false, "Show help messages.")
            .addOption("v", "version", false, "Print the version.")
            .addOption("q", "quiet", false, "Do not print debug logs.");
        final CommandLineParser cmdParser = new DefaultParser();
        final CommandLine cmd = cmdParser.parse(options, args);
        if (cmd.hasOption('h')) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar chrome-script-{app-version}-{java-version}.jar [options]", options);
        } else if (cmd.hasOption('v')) {
            final Logger logger = LoggerFactory.getLogger(Main.class);
            if (logger.isInfoEnabled()) {
                logger.info("chrome-script-0.0.1");
            }
        } else {
            if (cmd.hasOption('q')) {
                System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");
            }
        }
    }

}

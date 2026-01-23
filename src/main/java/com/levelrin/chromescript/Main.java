package com.levelrin.chromescript;

import com.levelrin.antlr.generated.MainGrammarLexer;
import com.levelrin.antlr.generated.MainGrammarParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(final String... args) throws ParseException, IOException {
        final Options options = new Options();
        options.addOption("h", "help", false, "Show help messages.")
            .addOption("v", "version", false, "Print the version.")
            .addOption("q", "quiet", false, "Do not print debug logs.")
            .addOption("i", "init", false, "Initialize the project in the current directory.");
        final CommandLineParser cmdParser = new DefaultParser();
        final CommandLine cmd = cmdParser.parse(options, args);
        final Logger logger = LoggerFactory.getLogger(Main.class);
        if (cmd.hasOption('h')) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar chrome-script-{app-version}-{java-version}.jar [options]", options);
        } else if (cmd.hasOption('v')) {
            if (logger.isInfoEnabled()) {
                logger.info("chrome-script-0.0.1");
            }
        } else if (cmd.hasOption('i')) {
            Files.createDirectories(Paths.get("images/icons"));
            Files.createDirectory(Paths.get("popup"));
            Files.createDirectories(Paths.get("scripts"));
            Files.writeString(
                Paths.get("popup/popup.html"),
                """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Chrome Script Project</title>
                </head>
                <body>
                <h1>Please modify this page.</h1>
                <script src="popup.js"></script>
                </body>
                </html>
                """,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );
            Files.writeString(
                Paths.get("manifest.json"),
                """
                {
                  "manifest_version":3,
                  "version":"0.0.1",
                  "name":"Chrome Script Project",
                  "description":"Please modify this file.",
                  "icons":{
                    "16":"images/icons/icon16.png",
                    "32":"images/icons/icon32.png",
                    "48":"images/icons/icon48.png",
                    "128":"images/icons/icon128.png"
                  },
                  "action":{
                    "default_popup":"popup/popup.html"
                  },
                  "permissions": ["tabs", "scripting"],
                  "host_permissions": [
                    "http://*/*",
                    "https://*/*"
                  ],
                  "background": {
                    "service_worker": "background.js"
                  }
                }
                """,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );
            Files.writeString(
                Paths.get(".gitattributes"),
                "* text=auto eol=lf\n",
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );
            logger.info("A new Chrome extension project has been initialized!");
            logger.info("Please modify the following files:");
            logger.info(" - manifest.json");
            logger.info(" - popup/popup.html");
            logger.info("Also, please put icon files specified in the manifest.json.");
            logger.info("Have a fun development journey :D");
        } else {
            if (cmd.hasOption('q')) {
                System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");
            }
            final Path mainScript = Paths.get("main.chr");
            if (Files.exists(mainScript)) {
                final String code = Files.readString(mainScript);
                final CharStream charStream = CharStreams.fromString(code);
                final MainGrammarLexer lexer = new MainGrammarLexer(charStream);
                final CommonTokenStream tokens = new CommonTokenStream(lexer);
                final MainGrammarParser parser = new MainGrammarParser(tokens);
                final ParseTree tree = parser.file();
                final MainGrammarListener listener = new MainGrammarListener();
                ParseTreeWalker.DEFAULT.walk(listener, tree);
                Files.writeString(
                    Paths.get("background.js"),
                    listener.backgroundFile().toString(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
                );
                Files.writeString(
                    Paths.get("popup/popup.js"),
                    String.format(
                        """
                        window.addEventListener("load", () => {
                            %s
                        });
                        """,
                        listener.popupFile()
                    ),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
                );
                for (int index = 0; index < listener.scriptFiles().size(); index++) {
                    Files.writeString(
                        Paths.get(String.format("scripts/%d.js", index)),
                        listener.scriptFiles().get(index).toString(),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                    );
                }
            } else {
                logger.error("Could not find the file: main.chr");
            }
        }
    }

}

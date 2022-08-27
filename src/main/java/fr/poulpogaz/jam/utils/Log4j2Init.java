package fr.poulpogaz.jam.utils;

import fr.poulpogaz.jam.Cache;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.nio.file.Path;

public class Log4j2Init {

    public static void init() {
        Path log = Cache.of("jam.log");

        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setStatusLevel(Level.INFO);

        LayoutComponentBuilder layout = builder.newLayout("PatternLayout");
        layout.addAttribute("pattern", "%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n");

        AppenderComponentBuilder console = builder.newAppender("stdout", "Console");
        console.add(layout);

        AppenderComponentBuilder file = builder.newAppender("file", "File");
        file.addAttribute("fileName", log.toString());
        file.addAttribute("append", true);
        file.add(layout);

        builder.add(console);
        builder.add(file);

        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.TRACE);
        rootLogger.add(builder.newAppenderRef("stdout"));
        rootLogger.add(builder.newAppenderRef("file"));
        builder.add(rootLogger);

        ((LoggerContext) LogManager.getContext(false)).reconfigure(builder.build());
    }
}

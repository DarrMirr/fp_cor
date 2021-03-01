package com.github.darrmirr.fp.logger;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Simple Logger implementation
 */
public interface Logger extends Consumer<LogEvent> {
    Logger CONSOLE = Logger.appenderConsole();

    static Logger appenderConsole() {
        return event -> System.out.println("[" + event.level + "] " + event.message);
    }

    static Predicate<LogEvent> level(Level level) {
        return event -> event != null && level == event.level;
    }

    enum Level {
        DEBUG, INFO, WARNING, ERROR
    }
}

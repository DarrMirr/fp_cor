package com.github.darrmirr.fp;

import com.github.darrmirr.fp.logger.LogEvent;
import com.github.darrmirr.fp.logger.Logger;

import static com.github.darrmirr.fp.logger.Logger.Level.*;
import static com.github.darrmirr.fp.utils.Chain.chain;

/**
 * Example 2:
 *
 *     Chain java.util.function.Consumer
 *     Example represent simple logger implementation
 *
 */
public interface MainLogger {

    static void main(String[] args) {
        LogEvent logEvent = new LogEvent(WARNING, "test message");

        var logger = chain(Logger.CONSOLE::accept).responsibility(Logger.level(DEBUG)::test)
                .chain(Logger.CONSOLE).responsibility(Logger.level(INFO)::test)
                .chain(Logger.CONSOLE).responsibility(Logger.level(WARNING)::test)
                .chain(Logger.CONSOLE).responsibility(Logger.level(ERROR)::test);

        logger.accept(logEvent);
    }
}

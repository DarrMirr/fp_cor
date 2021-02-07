package com.github.darrmirr.fp.logger;

public class LogEvent {
    public Logger.Level level;
    public String message;

    public LogEvent(Logger.Level level, String message) {
        this.level = level;
        this.message = message;
    }
}

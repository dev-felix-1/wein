package de.fekl.stat.util;

import java.util.HashMap;
import java.util.Map;

public class LogManager {

	private static final LogManager INSTANCE = new LogManager();

	private final Map<String, ILogger> loggerMap = new HashMap<>();

	public static LogManager getInstance() {
		return INSTANCE;
	}

	public ILogger getLogger(Class<?> clazz) {
		return loggerMap.computeIfAbsent(clazz.getName(), s -> new ConsoleLogger());
	}

	public void setLogger(Class<?> clazz, ILogger logger) {
		loggerMap.put(clazz.getName(), logger);
	}

}

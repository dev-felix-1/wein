package de.fekl.stat.util;

public interface ILogger {

	void trace(String format, Object... args);

	void debug(String format, Object... args);

	void info(String format, Object... args);

	void warn(String format, Object... args);

	void error(String format, Object... args);

	boolean isTraceEnable();

	boolean isDebugEnable();

	boolean isInfoEnable();

	boolean isWarnEnable();

	boolean isErrorEnable();

}

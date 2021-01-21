package de.fekl.stat.util;

public class ConsoleLogger implements ILogger {

	@Override
	public void trace(String format, Object... args) {
		if (isTraceEnable()) {
			System.out.println(String.format("TRACE: " + format, args));
		}
	}

	@Override
	public void debug(String format, Object... args) {
		if (isDebugEnable()) {
			System.out.println(String.format("DEBUG: " + format, args));
		}
	}

	@Override
	public void info(String format, Object... args) {
		if (isInfoEnable()) {
			System.out.println(String.format("INFO: " + format, args));
		}
	}

	@Override
	public void warn(String format, Object... args) {
		if (isWarnEnable()) {
			System.out.println(String.format("WARN: " + format, args));
		}
	}

	@Override
	public void error(String format, Object... args) {
		if (isErrorEnable()) {
			System.out.println(String.format("ERROR: " + format, args));
		}
	}

	@Override
	public boolean isTraceEnable() {
		return true;
	}

	@Override
	public boolean isDebugEnable() {
		return true;
	}

	@Override
	public boolean isInfoEnable() {
		return true;
	}

	@Override
	public boolean isWarnEnable() {
		return true;
	}

	@Override
	public boolean isErrorEnable() {
		return true;
	}

}

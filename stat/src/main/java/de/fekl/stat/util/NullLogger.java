package de.fekl.stat.util;

public class NullLogger implements ILogger {

	@Override
	public void trace(String format, Object... args) {

	}

	@Override
	public void debug(String format, Object... args) {

	}

	@Override
	public void info(String format, Object... args) {

	}

	@Override
	public void warn(String format, Object... args) {

	}

	@Override
	public void error(String format, Object... args) {

	}

	@Override
	public boolean isTraceEnable() {
		return false;
	}

	@Override
	public boolean isDebugEnable() {
		return false;
	}

	@Override
	public boolean isInfoEnable() {
		return false;
	}

	@Override
	public boolean isWarnEnable() {
		return false;
	}

	@Override
	public boolean isErrorEnable() {
		return false;
	}

}

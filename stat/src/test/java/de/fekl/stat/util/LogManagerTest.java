package de.fekl.stat.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LogManagerTest {

	@Test
	public void test() {
		StringBuilder sb = new StringBuilder();
		LogManager.getInstance().setLogger(getClass(), new NullLogger() {

			@Override
			public void info(String format, Object... args) {
				sb.append(format);
			}

		});
		ILogger logger = LogManager.getInstance().getLogger(getClass());
		logger.info("hello");
		Assertions.assertEquals("hello", sb.toString());
	}

}

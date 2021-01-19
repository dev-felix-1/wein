package de.fekl.baut;

public class LogManager {

	private static final LogManager INSTANCE = new LogManager();

	public static LogManager getInstance() {
		return INSTANCE;
	}

	private ILogger logger = new ConsoleLogger();

	public ILogger getLogger() {
		return logger;
	}
	
	public void setLogger(ILogger logger) {
		this.logger = logger;
	}

}

package de.fekl.stat.core.impl.state.net;

import de.fekl.dine.util.RandomNames;

public class ProcessingContainerNames {

	private ProcessingContainerNames() {

	}

	public static final String CONTAINER_NAME_PREFIX = "processing_container_";
	public static final int CONTAINER_NAME_LENGTH = 28;

	public static String generateName() {
		return RandomNames.getRandomName(ProcessingContainerNames.class.getName(), "processor_", 28);
	}
}

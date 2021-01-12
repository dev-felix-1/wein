package de.fekl.wein.api.core;

import de.fekl.baut.RandomNames;

public class ModuleNames {

	private ModuleNames() {

	}

	public static String generateName() {
		return RandomNames.getRandomName(ModuleNames.class.getName(), "module_", 15);
	}

}

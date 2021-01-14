package de.fekl.tran.impl;

import de.fekl.baut.RandomNames;

public class RouteNames {

	private RouteNames() {

	}

	public static String generateName() {
		return RandomNames.getRandomName(RouteNames.class.getName(), "route_", 13);
	}

}

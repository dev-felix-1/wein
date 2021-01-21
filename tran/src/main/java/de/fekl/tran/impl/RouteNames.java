package de.fekl.tran.impl;

import de.fekl.dine.util.RandomNames;

public class RouteNames {

	private RouteNames() {

	}

	public static String generateName() {
		return RandomNames.getRandomName(RouteNames.class.getName(), "route_", 13);
	}

}

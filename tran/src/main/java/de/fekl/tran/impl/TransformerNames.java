package de.fekl.tran.impl;

import de.fekl.baut.RandomNames;

public class TransformerNames {

	private TransformerNames() {

	}

	public static String generateTransformerName() {
		return RandomNames.getRandomName(TransformerNames.class.getName(), "trans_", 14);
	}

}

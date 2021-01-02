package de.fekl.cone.api.core;

import de.fekl.baut.ICopyFactory;

public interface IToken {

	ICopyFactory<IToken> getCopyFactory();

}

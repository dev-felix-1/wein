package de.fekl.cone.api.core;

import de.fekl.baut.ICopyFactory;

public class SimpleToken implements ITokenDeprecated {

	public SimpleToken() {
		super();
	}

	public SimpleToken(SimpleToken simpleToken) {
		this();
	}

	@Override
	public ICopyFactory<ITokenDeprecated> getCopyFactory() {
		return new ICopyFactory<ITokenDeprecated>() {

			@Override
			public ITokenDeprecated copy(ITokenDeprecated object) {
				return new SimpleToken();
			}

			@Override
			public ITokenDeprecated copy() {
				return copy(SimpleToken.this);
			}
		};
	}

}

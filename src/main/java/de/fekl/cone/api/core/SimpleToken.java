package de.fekl.cone.api.core;

import de.fekl.baut.ICopyFactory;

public class SimpleToken implements IToken {

	public SimpleToken() {
		super();
	}

	public SimpleToken(SimpleToken simpleToken) {
		this();
	}

	@Override
	public ICopyFactory<IToken> getCopyFactory() {
		return new ICopyFactory<IToken>() {

			@Override
			public IToken copy(IToken object) {
				return new SimpleToken();
			}

			@Override
			public IToken copy() {
				return copy(SimpleToken.this);
			}
		};
	}

}

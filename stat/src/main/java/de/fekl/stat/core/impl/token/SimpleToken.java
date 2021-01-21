package de.fekl.stat.core.impl.token;

import java.util.Objects;

import de.fekl.dine.util.Precondition;
import de.fekl.stat.core.api.token.IToken;

public class SimpleToken implements IToken {

	private final String id;

	public SimpleToken(String id) {
		Precondition.isNotEmpty(id);
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return String.format("%s('%s')", getClass().getSimpleName(), id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(IToken.class, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IToken other) {
			return id.equals(other.getId());
		}
		return false;
	}

}

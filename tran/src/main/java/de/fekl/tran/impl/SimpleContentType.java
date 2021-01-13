package de.fekl.tran.impl;

import java.util.Objects;

import de.fekl.baut.Precondition;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.IFormat;

public class SimpleContentType<T> implements IContentType<T> {

	private final Class<T> type;
	private final IFormat format;

	public SimpleContentType(Class<T> type, IFormat format) {
		Precondition.isNotNull(type);
		Precondition.isNotNull(format);
		this.type = type;
		this.format = format;
	}

	@Override
	public Class<T> getType() {
		return type;
	}

	@Override
	public IFormat getFormat() {
		return format;
	}

	@Override
	public int hashCode() {
		return Objects.hash(SimpleContentType.class, type, format);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IContentType other) {
			return other.getFormat().equals(format) && other.getType().equals(type);
		}
		return false;
	}

}

package de.fekl.tran.impl;

import java.util.List;

import de.fekl.tran.api.core.IAutoMergedMessage;
import de.fekl.tran.api.core.IContentType;

public class SimpleAutoMergedMessage extends SimpleMessage<List<?>> implements IAutoMergedMessage {

	private final List<IContentType<?>> contentTypes;

	public SimpleAutoMergedMessage(List<?> value, List<IContentType<?>> contentTypes) {
		super(value, null);
		this.contentTypes = contentTypes;
	}

	@Override
	public List<IContentType<?>> getContentTypes() {
		return contentTypes;
	}

}

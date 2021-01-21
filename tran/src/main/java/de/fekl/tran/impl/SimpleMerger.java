package de.fekl.tran.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.fekl.dine.util.Precondition;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.IMerge;
import de.fekl.tran.api.core.IMerger;
import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.ITransformation;

public class SimpleMerger<T> extends SimpleTransformer<List<?>, T> implements IMerger<T> {

	private final List<IContentType<?>> sourceContentTypes;

	@SuppressWarnings("unchecked")
	private final static IContentType<List<?>> CONTENT_TYPE = (IContentType<List<?>>) (IContentType<?>) StandardContentTypes.LIST_OF_OBJECTS;

	public SimpleMerger(List<IContentType<?>> sourceContentTypes, IContentType<T> targetContentType,
			ITransformation<List<?>, T> transformation, String id, boolean autoSplit) {
		super(CONTENT_TYPE, targetContentType, transformation, id, autoSplit);
		this.sourceContentTypes = sourceContentTypes;
		Precondition.hasClass(transformation, IMerge.class); 
	}

	@Override
	public List<IContentType<?>> getSourceContentTypes() {
		return Collections.unmodifiableList(sourceContentTypes);
	}

	@Override
	public IMessage<T> merge(List<IMessage<?>> messages) {
		List<?> values = messages.stream().map(m -> m.getValue()).collect(Collectors.toList());
		T transformedValues = getTransformation().transform(values);
		return new SimpleMessageFactory().createMessage(transformedValues);
	}

}

package de.fekl.tran.api.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fekl.baut.Precondition;
import de.fekl.tran.impl.SimpleTransformer;

public class SimpleMerger<T> extends SimpleTransformer<List<?>, T> implements IMerger<T> {
	
	private final List<IContentType<?>> sourceContentTypes = new ArrayList<>();

	public SimpleMerger(IContentType<List<?>> sourceContentType, IContentType<T> targetContentType,
			ITransformation<List<?>, T> transformation, String id, boolean autoSplit) {
		super(sourceContentType, targetContentType, transformation, id, autoSplit);
		Precondition.hasClass(transformation, IMerge.class);
	}

	@Override
	public List<IContentType<?>> getSourceContentTypes() {
		return Collections.unmodifiableList(sourceContentTypes);
	}


}

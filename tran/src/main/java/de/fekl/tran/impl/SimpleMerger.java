package de.fekl.tran.impl;

import java.util.Collections;
import java.util.List;

import de.fekl.baut.Precondition;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.IFormat;
import de.fekl.tran.api.core.IMerge;
import de.fekl.tran.api.core.IMerger;
import de.fekl.tran.api.core.ITransformation;

public class SimpleMerger<T> extends SimpleTransformer<List<?>, T> implements IMerger<T> {
	
	private final List<IContentType<?>> sourceContentTypes;
	
	private final static IContentType<List<?>> CONTENT_TYPE = new IContentType<List<?>>() {

		@Override
		public Class<List<?>> getType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IFormat getFormat() {
			// TODO Auto-generated method stub
			return null;
		}
	};

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


}

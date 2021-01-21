package de.fekl.tran.impl;

import de.fekl.dine.core.api.node.AbstractNodeBuilder;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.ITransformation;
import de.fekl.tran.api.core.ITransformer;
import de.fekl.tran.api.core.ITransformerFactory;

public class TransformerBuilder<S, T>
		extends AbstractNodeBuilder<ITransformer<?, ?>, ITransformerFactory, TransformerBuilder<S, T>> {

	private IContentType<S> sourceContentType;
	private IContentType<T> targetContentType;
	private ITransformation<S, T> transformation;
	private boolean autoSplit;

	public TransformerBuilder() {
		autoSplit(false);
		setAutoLookUp(true);
		setNodeFactory(new SimpleTransformerFactory());
	}

	public TransformerBuilder<S, T> autoSplit(boolean autoSplit) {
		this.autoSplit = autoSplit;
		return this;
	}

	public TransformerBuilder<S, T> source(IContentType<S> sourceContentType) {
		this.sourceContentType = sourceContentType;
		return this;
	}

	public TransformerBuilder<S, T> target(IContentType<T> targetContentType) {
		this.targetContentType = targetContentType;
		return this;
	}

	public TransformerBuilder<S, T> source(String sourceContentTypeName) {
		this.sourceContentType = StandardContentTypes.byName(sourceContentTypeName);
		return this;
	}

	public TransformerBuilder<S, T> target(String targetContentTypeName) {
		this.targetContentType = StandardContentTypes.byName(targetContentTypeName);
		return this;
	}

	public TransformerBuilder<S, T> transformation(ITransformation<S, T> transformation) {
		this.transformation = transformation;
		return this;
	}

	@SuppressWarnings("unchecked")
	public ITransformer<S, T> doBuild() {
		if (getId() == null || getId().isBlank()) {
			id(TransformerNames.generateTransformerName());
		}
		if (transformation == null) {
			if (targetContentType != null && sourceContentType != null
					&& targetContentType.getType().isAssignableFrom(sourceContentType.getType())) {
				transformation = (S o) -> (T) o;
			}
		}
		return getNodeFactory().createTransformer(sourceContentType, targetContentType, transformation, getId(),
				autoSplit);
	}
	
	protected boolean isAutoSplit() {
		return autoSplit;
	}
	
	protected IContentType<S> getSourceContentType() {
		return sourceContentType;
	}
	
	protected IContentType<T> getTargetContentType() {
		return targetContentType;
	}
	
	protected ITransformation<S, T> getTransformation() {
		return transformation;
	}
}

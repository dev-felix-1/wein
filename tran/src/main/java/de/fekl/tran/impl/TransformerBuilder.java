package de.fekl.tran.impl;

import de.fekl.dine.api.node.AbstractNodeBuilder;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.ITransformation;
import de.fekl.tran.api.core.ITransformer;
import de.fekl.tran.api.core.ITransformerFactory;
import de.fekl.tran.api.core.ITransformerRegistry;

public class TransformerBuilder<S, T>
		extends AbstractNodeBuilder<ITransformer, ITransformerFactory, TransformerBuilder<S, T>> {

	private IContentType<S> sourceContentType;
	private IContentType<T> targetContentType;
	private ITransformation<S, T> transformation;
	private ITransformerRegistry transformerRegistry;

	public TransformerBuilder() {
		setNodeFactory(new SimpleTransformerFactory());
	}

	public TransformerBuilder<S, T> setTransformerRegistry(ITransformerRegistry transformerRegistry) {
		this.transformerRegistry = transformerRegistry;
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

	public ITransformer<S, T> build() {
		ITransformer<S, T> result = null;
		if (getId() != null && !getId().isBlank() && targetContentType == null && sourceContentType == null
				&& transformation == null) {
			result = lookUpTransformer();
		} else {
			result = createTransformer();
		}
		return result;
	}

	protected ITransformer<S, T> createTransformer() {
		if (getId() == null || getId().isBlank()) {
			id(TransformerNames.generateTransformerName());
		}
		if (transformation == null) {
			if (targetContentType != null && sourceContentType != null
					&& targetContentType.getType().isAssignableFrom(sourceContentType.getType())) {
				transformation = (S o) -> (T) o;
			}
		}
		return getNodeFactory().createTransformer(sourceContentType, targetContentType, transformation, getId());
	}

	protected ITransformer<S, T> lookUpTransformer() {
		ITransformer<S, T> transformer = transformerRegistry.getTransformer(getId());
		if (transformer == null) {
			throw new IllegalStateException(String.format("Did not find transformer with id %s in registry",getId()));
		}
		return transformer;
	}
}

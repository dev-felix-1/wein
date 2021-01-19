package de.fekl.tran.impl;

import java.util.ArrayList;
import java.util.List;

import de.fekl.dine.api.node.AbstractNodeBuilder;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.IMerge;
import de.fekl.tran.api.core.IMerger;
import de.fekl.tran.api.core.ITransformer;
import de.fekl.tran.api.core.ITransformerFactory;

public class MergerBuilder<T> extends AbstractNodeBuilder<ITransformer<?, ?>, ITransformerFactory, MergerBuilder<T>> {

	private List<IContentType<?>> sourceContentTypes = new ArrayList<>();
	private IContentType<T> targetContentType;
	private IMerge<T> transformation;
	private boolean autoSplit;

	public MergerBuilder() {
		autoSplit(false);
		setAutoLookUp(true);
		setNodeFactory(new SimpleTransformerFactory());
	}

	public MergerBuilder<T> autoSplit(boolean autoSplit) {
		this.autoSplit = autoSplit;
		return this;
	}

	public MergerBuilder<T> source(IContentType<?> sourceContentType) {
		this.sourceContentTypes.add(sourceContentType);
		return this;
	}

	public MergerBuilder<T> target(IContentType<T> targetContentType) {
		this.targetContentType = targetContentType;
		return this;
	}

	public MergerBuilder<T> source(String sourceContentTypeName) {
		this.sourceContentTypes.add(StandardContentTypes.byName(sourceContentTypeName));
		return this;
	}

	public MergerBuilder<T> target(String targetContentTypeName) {
		this.targetContentType = StandardContentTypes.byName(targetContentTypeName);
		return this;
	}

	public MergerBuilder<T> transformation(IMerge<T> transformation) {
		this.transformation = transformation;
		return this;
	}

	public IMerger<T> doBuild() {
		if (getId() == null || getId().isBlank()) {
			id(TransformerNames.generateTransformerName());
		}
		return getNodeFactory().createMerger(sourceContentTypes, targetContentType, transformation, getId(),autoSplit);
	}
}

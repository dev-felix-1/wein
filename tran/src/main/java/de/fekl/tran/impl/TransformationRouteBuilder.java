package de.fekl.tran.impl;

import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.dine.api.tree.SpongeNetBuilder;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.ITransformationRoute;
import de.fekl.tran.api.core.ITransformer;

public class TransformationRouteBuilder<S, T> {

	private String id;
	private IContentType<S> source;
	private IContentType<T> target;
	private ISpongeNet<ITransformer<?,?>> transformationNet;

	public TransformationRouteBuilder(SpongeNetBuilder<ITransformer<?,?>> netBuilder) {
		this();
		transformation(netBuilder);
	}

	public TransformationRouteBuilder() {
		super();
	}

	public TransformationRouteBuilder<S, T> source(IContentType<S> source) {
		this.source = source;
		return this;
	}

	public TransformationRouteBuilder<S, T> target(IContentType<T> target) {
		this.target = target;
		return this;
	}

	public TransformationRouteBuilder<S, T> transformation(ISpongeNet<ITransformer<?,?>> transformationNet) {
		this.transformationNet = transformationNet;
		return this;
	}

	public TransformationRouteBuilder<S, T> transformation(SpongeNetBuilder<ITransformer<?,?>> netBuilder) {
		this.transformationNet = netBuilder.build();
		return this;
	}
	
	public TransformationRouteBuilder<S, T> id(String id) {
		this.id = id;
		return this;
	}

	@SuppressWarnings("unchecked")
	public ITransformationRoute<S, T> build() {
		if (id == null || id.isBlank()) {
			id = TransformerNames.generateTransformerName();
		}
		if (source == null) {
			source = (IContentType<S>) transformationNet.getRoot().getSourceContentType();
		}
		if (target == null) {
			target = (IContentType<T>) transformationNet.getLeafs().iterator().next().getTargetContentType();
		}
		return new SimpleTransformationRoute<S,T>(id,source, target, transformationNet);
	}
}

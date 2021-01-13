package de.fekl.tran.impl;

import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.dine.api.tree.SpongeNetBuilder;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.ITransformationRoute;
import de.fekl.tran.api.core.ITransformer;

public class TransformationRouteBuilder<S, T> {

	private IContentType<S> source;
	private IContentType<T> target;
	private ISpongeNet<ITransformer> transformationNet;

	public TransformationRouteBuilder(SpongeNetBuilder<ITransformer> netBuilder) {
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

	public TransformationRouteBuilder<S, T> transformation(ISpongeNet<ITransformer> transformationNet) {
		this.transformationNet = transformationNet;
		return this;
	}

	public TransformationRouteBuilder<S, T> transformation(SpongeNetBuilder<ITransformer> netBuilder) {
		this.transformationNet = netBuilder.build();
		return this;
	}

	public ITransformationRoute<S, T> build() {
		if (source == null) {
			source = transformationNet.getRoot().getSourceContentType();
		}
		if (target == null) {
			target = transformationNet.getLeafs().iterator().next().getTargetContentType();
		}
		return new SimpleTransformationRoute<>(source, target, transformationNet);
	}

}

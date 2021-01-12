package de.fekl.tran;

import de.fekl.baut.Precondition;
import de.fekl.dine.api.tree.ISpongeNet;

@SuppressWarnings("rawtypes")
public class SimpleTransformationRoute<S, T> implements ITransformationRoute<S, T> {

	private final ISpongeNet<ITransformer> graph;
	private final IContentType<S> sourceContentType;
	private final IContentType<T> targetContentType;

	public SimpleTransformationRoute(IContentType<S> sourceContentType, IContentType<T> targetContentType,
			ISpongeNet<ITransformer> spongeNet) {
		Precondition.isNotNull(sourceContentType);
		Precondition.isNotNull(targetContentType);
		Precondition.isNotNull(spongeNet);
		if (spongeNet.getLeafs().size() > 1) {
			throw new IllegalArgumentException("A Route can not have more than one end node");
		}
		if (((ITransformer) spongeNet.getRoot()).getSourceContentType() != sourceContentType) {
			throw new IllegalArgumentException(String.format("ContentTypes do not match %s <=> %s",
					((ITransformer) spongeNet.getRoot()).getSourceContentType(), sourceContentType));
		}
		if (((ITransformer) spongeNet.getLeafs().iterator().next()).getTargetContentType() != targetContentType) {
			throw new IllegalArgumentException(String.format("ContentTypes do not match %s <=> %s",
					((ITransformer) spongeNet.getLeafs().iterator().next()).getTargetContentType(), targetContentType));
		}
		graph = spongeNet;
		this.sourceContentType = sourceContentType;
		this.targetContentType = targetContentType;
	}

	@Override
	public ISpongeNet<ITransformer> getGraph() {
		return graph;
	}

	@Override
	public ITransformer<S, ?> getFirst() {
		return (ITransformer<S, ?>) graph.getRoot();
	}

	@Override
	public ITransformer<?, T> getLast() {
		return graph.getLeafs().iterator().next();
	}

	@Override
	public IContentType<S> getSourceContentType() {
		return sourceContentType;
	}

	@Override
	public IContentType<T> getTargetContentType() {
		return targetContentType;
	}

}

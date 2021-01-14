package de.fekl.tran.impl;

import java.util.stream.Collectors;

import de.fekl.baut.Precondition;
import de.fekl.dine.api.edge.IEdge;
import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.ITransformationRoute;
import de.fekl.tran.api.core.ITransformer;

@SuppressWarnings("rawtypes")
public class SimpleTransformationRoute<S, T> implements ITransformationRoute<S, T> {

	private final String id;
	private final ISpongeNet<ITransformer> graph;
	private final IContentType<S> sourceContentType;
	private final IContentType<T> targetContentType;

	public SimpleTransformationRoute(String id, IContentType<S> sourceContentType, IContentType<T> targetContentType,
			ISpongeNet<ITransformer> spongeNet) {
		Precondition.isNotEmpty(id);
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
		this.id = id;
	}

	@Override
	public ISpongeNet<ITransformer> getGraph() {
		return graph;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ITransformer<S, ?> getFirst() {
		return (ITransformer<S, ?>) graph.getRoot();
	}

	@SuppressWarnings("unchecked")
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

	private <U, V> String printTransformer(ITransformer<U, V> t) {
		return printTransformer(t.getId());
	}

	private String printTransformer(String nodeId) {
		if (nodeId.equals(graph.getRoot().getId())) {
			return String.format("(%s)", nodeId);
		} else if (graph.getLeafs().stream().anyMatch(leaf -> leaf.getId().equals(nodeId))) {
			return String.format("<%s>", nodeId);
		} else {
			return nodeId;
		}
	}

	private String printEdge(IEdge edge) {
		return String.format("{%s -> %s}", printTransformer(edge.getSource()), printTransformer(edge.getTarget()));
	}

	@Override
	public String toString() {
		var printTemplate = "%s: %s";
		if (graph.getEdges().isEmpty()) {
			return String.format(printTemplate, this.getClass().getSimpleName(),
					graph.getNodes().stream().map(this::printTransformer).collect(Collectors.joining(", ")));
		} else {
			return String.format(printTemplate, this.getClass().getSimpleName(),
					graph.getEdges().stream().map(this::printEdge).collect(Collectors.joining(", ")));
		}
	}

	@Override
	public String getId() {
		return id;
	}

}

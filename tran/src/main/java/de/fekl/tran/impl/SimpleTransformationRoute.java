package de.fekl.tran.impl;

import java.util.List;
import java.util.stream.Collectors;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.dine.util.Precondition;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.IMerger;
import de.fekl.tran.api.core.ITransformationRoute;
import de.fekl.tran.api.core.ITransformer;

public class SimpleTransformationRoute<S, T> implements ITransformationRoute<S, T> {

	private final String id;
	private final ISpongeNet<ITransformer<?, ?>> graph;
	private final IContentType<S> sourceContentType;
	private final IContentType<T> targetContentType;

	public SimpleTransformationRoute(String id, IContentType<S> sourceContentType, IContentType<T> targetContentType,
			ISpongeNet<ITransformer<?, ?>> spongeNet) {
		Precondition.isNotEmpty(id);
		Precondition.isNotNull(sourceContentType);
		Precondition.isNotNull(targetContentType);
		Precondition.isNotNull(spongeNet);
		if (spongeNet.getLeafs().size() > 1) {
			throw new IllegalArgumentException(
					String.format("A Route can not have more than one end node. Found end nodes: [%s]",
							spongeNet.getLeafs().stream().map(ITransformer::getId).collect(Collectors.joining(", "))));
		}
		if (((ITransformer<?, ?>) spongeNet.getRoot()).getSourceContentType() != sourceContentType) {
			throw new IllegalArgumentException(String.format("ContentTypes do not match %s <=> %s",
					((ITransformer<?, ?>) spongeNet.getRoot()).getSourceContentType(), sourceContentType));
		}
		if (((ITransformer<?, ?>) spongeNet.getLeafs().iterator().next()).getTargetContentType() != targetContentType) {
			throw new IllegalArgumentException(String.format("ContentTypes do not match %s <=> %s",
					((ITransformer<?, ?>) spongeNet.getLeafs().iterator().next()).getTargetContentType(),
					targetContentType));
		}
		checkConnectedTransformersContentTypeMatching(spongeNet.getRoot(), spongeNet);

		graph = spongeNet;
		this.sourceContentType = sourceContentType;
		this.targetContentType = targetContentType;
		this.id = id;
	}

	static <U, V> void checkConnectedTransformersContentTypeMatching(ITransformer<U, V> transformer,
			ISpongeNet<ITransformer<?, ?>> net) {
		ITransformer<?, ?> current = checkConnectedTransformersContentTypeMatchingOnOneWayPath(transformer, net);
		List<IEdge> outgoingEdges = net.getOutgoingEdges(current.getId());
		for (IEdge edge : outgoingEdges) {
			ITransformer<?, ?> nextNode = net.getNode(edge.getTarget());
			checkConnection(current, nextNode, net);
			checkConnectedTransformersContentTypeMatching(nextNode, net);
		}
	}

	static ITransformer<?, ?> checkConnectedTransformersContentTypeMatchingOnOneWayPath(ITransformer<?, ?> transformer,
			ISpongeNet<ITransformer<?, ?>> net) {
		ITransformer<?, ?> current = transformer;
		List<IEdge> outgoingEdges = null;
		while ((outgoingEdges = net.getOutgoingEdges(current.getId())).size() == 1) {
			ITransformer<?, ?> next = net.getNode(outgoingEdges.get(0).getTarget());
			checkConnection(current, next, net);
			current = next;
		}
		return current;
	}

	static <U, V> void checkConnection(ITransformer<?, U> transformerA, ITransformer<V, ?> transformerB,
			ISpongeNet<ITransformer<?, ?>> net) {
		if (transformerB instanceof IMerger<?>merger) {
			List<IContentType<?>> sourceContentTypes = merger.getSourceContentTypes();
			List<IEdge> incomingEdges = net.getIncomingEdges(merger.getId());
			if (sourceContentTypes.size() != incomingEdges.size()) {
				throw new IllegalStateException(String.format(
						"Merger %s has %s incoming edges (%s) but specifies %s content-types (%s)", merger.getId(),
						incomingEdges.size(), incomingEdges, sourceContentTypes.size(), sourceContentTypes));
			}
			for (int i = 0; i < incomingEdges.size(); i++) {
				String incoming = incomingEdges.get(i).getSource();
				IContentType<?> incomingTargetContentType = net.getNode(incoming).getTargetContentType();
				IContentType<?> mergerSourceContentType = sourceContentTypes.get(i);
				if (!incomingTargetContentType.equals(mergerSourceContentType)) {
					throw new IllegalStateException(
							String.format("(%s targetContentType %s) does not match (%s sourceContentType %s)",
									merger.getId(), mergerSourceContentType, incoming, incomingTargetContentType));
				}
			}
		} else {
			if (!transformerB.getSourceContentType().equals(transformerA.getTargetContentType())) {
				throw new IllegalStateException(
						String.format("(%s targetContentType %s) does not match (%s sourceContentType %s)",
								transformerA.getId(), transformerA.getTargetContentType(), transformerB.getId(),
								transformerB.getSourceContentType()));
			}
		}
	}

	@Override
	public ISpongeNet<ITransformer<?, ?>> getGraph() {
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
		return (ITransformer<?, T>) graph.getLeafs().iterator().next();
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

package de.fekl.stat.core.impl.state.net;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import de.fekl.dine.core.api.edge.IEdge;
import de.fekl.stat.core.api.state.operations.IStateChangeOperation;
import de.fekl.stat.core.api.state.operations.ITokenCreationOperation;
import de.fekl.stat.core.api.state.operations.ITokenMergeOperation;
import de.fekl.stat.core.api.state.operations.ITokenRemovalOperation;
import de.fekl.stat.core.api.state.operations.ITokenTransitionOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenFactory;
import de.fekl.stat.core.api.token.ITokenStore;
import de.fekl.stat.core.impl.token.SimpleTokenStore;

public class ColouredNetOperations {

	private static <T extends IToken> ITokenStore<T> cloneTokenStore(ITokenStore<T> toCopyFrom) {
		ITokenStore<T> newState = new SimpleTokenStore<T>();
		toCopyFrom.getTokenMapping().forEach((k, s) -> {
			s.forEach(v -> newState.putToken(k, v));
		});
		return newState;
	}

	public static class PutToken<T extends IToken> implements ITokenCreationOperation<T> {

		private String nodeId;
		private T token;

		private PutToken(String sourceNodeId, T token) {
			this.nodeId = sourceNodeId;
			this.token = token;
		}

		@Override
		public String toString() {
			return String.format("Put Token %s on %s", token.getId(), nodeId);
		}

		@Override
		public ITokenStore<T> apply(ITokenStore<T> state) {
			ITokenStore<T> newState = cloneTokenStore(state);
			newState.putToken(nodeId, token);
			return newState;
		}

		@Override
		public T getCreatedToken() {
			return token;
		}

		@Override
		public String getTargetNodeId() {
			return nodeId;
		}
	}

	public static class RemoveToken<T extends IToken> implements ITokenRemovalOperation<T> {

		private String targetNodeId;
		private T token;

		private RemoveToken(String targetNodeId, T token) {
			this.targetNodeId = targetNodeId;
			this.token = token;
		}

		@Override
		public String toString() {
			return String.format("Remove Token %s from %s", token, targetNodeId);
		}

		@Override
		public ITokenStore<T> apply(ITokenStore<T> state) {
			ITokenStore<T> newState = cloneTokenStore(state);
			newState.removeToken(targetNodeId, token);
			return newState;
		}

		@Override
		public T getRemovedToken() {
			return token;
		}

		@Override
		public String getTargetNodeId() {
			return targetNodeId;
		}
	}

	public static class MergeToken<T extends IToken> implements ITokenMergeOperation<T> {

		private String targetNodeId;
		private List<T> tokens;
		private T resultToken;

		private MergeToken(ITokenFactory<T> factory, String targetNodeId, T[] tokens) {
			this.targetNodeId = targetNodeId;
			this.tokens = Arrays.asList(tokens);
			this.resultToken = factory.mergeToken(tokens);
		}

		private MergeToken(ITokenFactory<T> factory, String targetNodeId, List<T> tokens) {
			this.targetNodeId = targetNodeId;
			this.tokens = tokens;
			this.resultToken = factory.mergeToken(tokens);
		}

		@Override
		public String toString() {
			return String.format("Merge Tokens ...");
		}

		@Override
		public ITokenStore<T> apply(ITokenStore<T> state) {
			ITokenStore<T> newState = cloneTokenStore(state);
			for (T token : tokens) {
				Set<T> tokensOnNode = state.getTokens(targetNodeId);
				if (!tokensOnNode.contains(token)) {
					throw new IllegalStateException(
							String.format("Token %s cannot be found on node %s", token, targetNodeId));
				}
				newState.removeToken(targetNodeId, token);
			}
			newState.putToken(targetNodeId, resultToken);
			return newState;
		}

		@Override
		public String getTargetNodeId() {
			return targetNodeId;
		}

		@Override
		public List<T> mergedTokens() {
			return tokens;
		}

		@Override
		public T getResultToken() {
			return resultToken;
		}
	}

	public static class MoveToken<T extends IToken> implements ITokenTransitionOperation<T> {

		private String sourceNodeId;
		private String targetNodeId;
		private T token;

		private MoveToken(String sourceNodeId, String targetNodeId, T token) {
			this.sourceNodeId = sourceNodeId;
			this.targetNodeId = targetNodeId;
			this.token = token;
		}

		@Override
		public String toString() {
			return String.format("Move Token %s from %s to %s", token, sourceNodeId, targetNodeId);
		}

		@Override
		public ITokenStore<T> apply(ITokenStore<T> state) {
			ITokenStore<T> newState = cloneTokenStore(state);
			T removedToken = newState.removeToken(sourceNodeId, token);
			if (removedToken == null) {
				throw new IllegalStateException(String.format("Cannot find token %s on node %s", token, sourceNodeId));
			}
			newState.putToken(targetNodeId, token);
			return newState;
		}

		@Override
		public T getTransitionedToken() {
			return token;
		}

		@Override
		public String getTargetNodeId() {
			return targetNodeId;
		}

		@Override
		public String getSourceNodeId() {
			return sourceNodeId;
		}
	}

	public static class CopyToken<T extends IToken> implements ITokenCreationOperation<T> {

		private final String targetNodeId;
		private final T token;

		private CopyToken(String targetNodeId, T token, ITokenFactory<T> factory) {
			this.token = factory.copyToken(token);
			this.targetNodeId = targetNodeId;
		}

		@Override
		public String toString() {
			return String.format("Copy Token %s", token);
		}

		@Override
		public ITokenStore<T> apply(ITokenStore<T> state) {
			ITokenStore<T> newState = cloneTokenStore(state);
			newState.putToken(targetNodeId, token);
			return newState;
		}

		@Override
		public String getTargetNodeId() {
			return targetNodeId;
		}

		@Override
		public T getCreatedToken() {
			return token;
		}
	}

	private ColouredNetOperations() {

	}

	public static <T extends IToken> IStateChangeOperation<ITokenStore<T>> putToken(String nodeId, T token) {
		return new PutToken<>(nodeId, token);
	}

	public static <T extends IToken> IStateChangeOperation<ITokenStore<T>> removeToken(String nodeId, T token) {
		return new RemoveToken<>(nodeId, token);
	}

	public static <T extends IToken> IStateChangeOperation<ITokenStore<T>> moveToken(String sourceNodeId,
			String targetNodeId, T token) {
		return new MoveToken<>(sourceNodeId, targetNodeId, token);
	}
	
	public static <T extends IToken> IStateChangeOperation<ITokenStore<T>> moveToken(IEdge edge, T token) {
		return new MoveToken<>(edge.getSource(), edge.getTarget(), token);
	}

	public static <T extends IToken, F extends ITokenFactory<T>> ITokenCreationOperation<T> copyToken(
			String targetNodeId, T token, F factory) {
		return new CopyToken<>(targetNodeId, token, factory);
	}

	public static <T extends IToken, F extends ITokenFactory<T>> ITokenMergeOperation<T> mergeToken(String targetNodeId,
			List<T> tokens, F factory) {
		return new MergeToken<>(factory, targetNodeId, tokens);
	}

}

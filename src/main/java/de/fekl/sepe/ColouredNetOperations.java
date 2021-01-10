package de.fekl.sepe;

import java.util.stream.IntStream;

import de.fekl.dine.api.state.IToken;
import de.fekl.dine.api.state.ITokenFactory;
import de.fekl.dine.api.state.ITokenStore;
import de.fekl.dine.api.state.SimpleTokenStore;
import de.fekl.esta.api.core.IStateChangeOperation;

public class ColouredNetOperations {

	private static ITokenStore cloneTokenStore(ITokenStore toCopyFrom) {
		ITokenStore newState = new SimpleTokenStore();
		toCopyFrom.getTokenMapping().forEach((k, s) -> {
			s.forEach(v -> newState.putToken(k, v));
		});
		return newState;
	}

	static class PutToken implements IStateChangeOperation<ITokenStore> {

		private String nodeId;
		private IToken token;

		PutToken(String sourceNodeId, IToken token) {
			this.nodeId = sourceNodeId;
			this.token = token;
		}

		@Override
		public String toString() {
			return String.format("Put Token %s on %s", token.getId(), nodeId);
		}

		@Override
		public ITokenStore apply(ITokenStore state) {
			ITokenStore newState = cloneTokenStore(state);
			newState.putToken(nodeId, token);
			return newState;
		}
	}

	static class RemoveToken implements IStateChangeOperation<ITokenStore> {

		private String sourceNodeId;
		private String tokenId;

		RemoveToken(String sourceNodeId, String tokenId) {
			this.sourceNodeId = sourceNodeId;
			this.tokenId = tokenId;
		}

		@Override
		public String toString() {
			return String.format("Remove Token %s from %s", tokenId, sourceNodeId);
		}

		@Override
		public ITokenStore apply(ITokenStore state) {
			ITokenStore newState = cloneTokenStore(state);
			newState.removeToken(sourceNodeId, tokenId);
			return newState;
		}
	}

	static class MoveToken implements IStateChangeOperation<ITokenStore> {

		private String sourceNodeId;
		private String targetNodeId;
		private String tokenId;

		MoveToken(String sourceNodeId, String targetNodeId, String tokenId) {
			this.sourceNodeId = sourceNodeId;
			this.targetNodeId = targetNodeId;
			this.tokenId = tokenId;
		}

		@Override
		public String toString() {
			return String.format("Move Token %s from %s to %s", tokenId, sourceNodeId, targetNodeId);
		}

		@Override
		public ITokenStore apply(ITokenStore state) {
			IToken token = state.getToken(tokenId);
			ITokenStore newState = cloneTokenStore(state);
			newState.removeToken(sourceNodeId, tokenId);
			newState.putToken(targetNodeId, token);
			return newState;
		}
	}

	static class CopyToken implements IStateChangeOperation<ITokenStore> {

		private ITokenFactory factory;
		private String tokenId;
		private int numberOfCopies;

		CopyToken(String tokenId, int numberOfCopies, ITokenFactory factory) {
			this.factory = factory;
			this.tokenId = tokenId;
			this.numberOfCopies = numberOfCopies;
		}

		@Override
		public String toString() {
			return String.format("Copy Token %s %s times", tokenId, numberOfCopies);
		}

		@Override
		public ITokenStore apply(ITokenStore state) {
			IToken token = state.getToken(tokenId);
			ITokenStore newState = cloneTokenStore(state);
			String nodeId = newState.getPosition(tokenId);
			IntStream.range(0, numberOfCopies).forEach(i -> newState.putToken(nodeId, factory.copyToken(token)));
			return newState;
		}
	}

	private ColouredNetOperations() {

	}

	public static IStateChangeOperation<ITokenStore> putToken(String nodeId, String tokenId, IToken token) {
		return new PutToken(nodeId, token);
	}

	public static IStateChangeOperation<ITokenStore> removeToken(String nodeId, String tokenId) {
		return new RemoveToken(nodeId, tokenId);
	}

	public static IStateChangeOperation<ITokenStore> moveToken(String sourceNodeId, String targetNodeId,
			String tokenId) {
		return new MoveToken(sourceNodeId, targetNodeId, tokenId);
	}

	public static IStateChangeOperation<ITokenStore> copyToken(String tokenId, int numberOfCopies,
			ITokenFactory factory) {
		return new CopyToken(tokenId, numberOfCopies, factory);
	}

}

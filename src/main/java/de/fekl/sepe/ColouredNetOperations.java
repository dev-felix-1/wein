package de.fekl.sepe;

import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import de.fekl.cone.api.core.IColouredNet;
import de.fekl.cone.api.core.IToken;
import de.fekl.cone.api.core.SimpleColouredNet;
import de.fekl.cone.api.core.TokenNames;
import de.fekl.esta.api.core.IStateChangeOperation;

public class ColouredNetOperations {

	static class PutToken implements IStateChangeOperation<IColouredNet> {

		private String nodeId;
		private String tokenId;
		private IToken token;

		PutToken(String sourceNodeId, String tokenId, IToken token) {
			this.nodeId = sourceNodeId;
			this.tokenId = tokenId;
			this.token = token;
		}

		@Override
		public String toString() {
			return String.format("Put Token %s(%s) on %s", tokenId, token, nodeId);
		}

		@Override
		public IColouredNet apply(IColouredNet state) {
			IColouredNet newState = new SimpleColouredNet(state);
			newState.putToken(nodeId, tokenId, token);
			return newState;
		}
	}

	static class RemoveToken implements IStateChangeOperation<IColouredNet> {

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
		public IColouredNet apply(IColouredNet state) {
			IColouredNet newState = new SimpleColouredNet(state);
			newState.removeToken(sourceNodeId, tokenId);
			return newState;
		}
	}

	static class MoveToken implements IStateChangeOperation<IColouredNet> {

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
		public IColouredNet apply(IColouredNet state) {
			IToken token = state.getToken(tokenId);
			IColouredNet newState = new SimpleColouredNet(state);
			newState.removeToken(sourceNodeId, tokenId);
			newState.putToken(targetNodeId, tokenId, token);
			return newState;
		}
	}

	static class CopyToken implements IStateChangeOperation<IColouredNet> {

		private String tokenId;
		private int numberOfCopies;

		CopyToken(String tokenId, int numberOfCopies) {
			this.tokenId = tokenId;
			this.numberOfCopies = numberOfCopies;
		}

		@Override
		public String toString() {
			return String.format("Copy Token %s %s times", tokenId, numberOfCopies);
		}

		@Override
		public IColouredNet apply(IColouredNet state) {
			IToken token = state.getToken(tokenId);
			IColouredNet newState = new SimpleColouredNet(state);
			Map<String, String> tokenToNodeMapping = newState.getTokenToNodeMapping();
			String nodeId = tokenToNodeMapping.get(tokenId);
			IntStream.range(0, numberOfCopies).forEach(
					i -> newState.putToken(nodeId, TokenNames.generateTokenName(), token.getCopyFactory().copy()));
			return newState;
		}
	}

	private ColouredNetOperations() {

	}

	public static IStateChangeOperation<IColouredNet> putToken(String nodeId, String tokenId, IToken token) {
		return new PutToken(nodeId, tokenId, token);
	}

	public static IStateChangeOperation<IColouredNet> removeToken(String nodeId, String tokenId) {
		return new RemoveToken(nodeId, tokenId);
	}

	public static IStateChangeOperation<IColouredNet> moveToken(String sourceNodeId, String targetNodeId,
			String tokenId) {
		return new MoveToken(sourceNodeId, targetNodeId, tokenId);
	}

	public static IStateChangeOperation<IColouredNet> copyToken(String tokenId, int numberOfCopies) {
		return new CopyToken(tokenId, numberOfCopies);
	}

}

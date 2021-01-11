package de.fekl.wein.api.core;

import de.fekl.dine.api.state.ITokenStore;
import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.esta.api.core.IStateContainer;
import de.fekl.sepe.ColouredNetProcessingContainer;

@SuppressWarnings("rawtypes")
public class TransformationNetProcessingContainer
		extends ColouredNetProcessingContainer<ITransformer, MessageContainer> {

	public TransformationNetProcessingContainer(ISpongeNet<ITransformer> net,
			ITokenStore<MessageContainer> initialState) {
		super(net, initialState, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <C extends IStateContainer<ITokenStore<MessageContainer>>> void handleToken(C stateContainer,
			String tokenId, String sourceNodeId) {
		ITransformer node = getNet().getNode(sourceNodeId);
		MessageContainer token = stateContainer.getCurrentState().getToken(tokenId);
		IMessage<?> transformedMessage = node.transform(token.getMessage());
		((MessageContainer) token).setMessage(transformedMessage);
		super.handleToken(stateContainer, tokenId, sourceNodeId);
	}

}

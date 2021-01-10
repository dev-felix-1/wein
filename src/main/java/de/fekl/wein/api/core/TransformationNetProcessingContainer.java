package de.fekl.wein.api.core;

import de.fekl.dine.api.graph.INode;
import de.fekl.dine.api.state.IToken;
import de.fekl.dine.api.state.ITokenStore;
import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.esta.api.core.IStateContainer;
import de.fekl.sepe.ColouredNetProcessingContainer;

public class TransformationNetProcessingContainer extends ColouredNetProcessingContainer {

	public TransformationNetProcessingContainer(ISpongeNet net, ITokenStore initialState) {
		super(net, initialState);
	}

	@Override
	protected void handleToken(IStateContainer<ITokenStore> stateContainer, String tokenId, String sourceNodeId) {
		INode node = getNet().getNode(sourceNodeId);
		if (node instanceof ITransformer) {
			IToken token = stateContainer.getCurrentState().getToken(tokenId);
			if (token instanceof IMessage) {
				IMessage transformedMessage = ((ITransformer) node).transform(((IMessage) token));
			}
		}
		super.handleToken(stateContainer, tokenId, sourceNodeId);
	}

}

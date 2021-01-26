package de.fekl.tran.impl;

import java.util.ArrayList;
import java.util.List;

import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.stat.core.api.state.operations.ITokenCreationOperation;
import de.fekl.stat.core.api.state.operations.ITokenMergeOperation;
import de.fekl.stat.core.api.state.operations.ITokenTransitionOperation;
import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenStore;
import de.fekl.stat.core.impl.state.net.SimpleColouredNetProcessingContainer;
import de.fekl.stat.core.impl.token.SimpleTokenStore;
import de.fekl.stat.util.ILogger;
import de.fekl.stat.util.LogManager;
import de.fekl.tran.api.core.IMerger;
import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.IMessageContainer;
import de.fekl.tran.api.core.ITransformer;

public class TransformationNetProcessingContainer
		extends SimpleColouredNetProcessingContainer<ITransformer<?, ?>, MessageContainer> {

	private static final ILogger LOG = LogManager.getInstance().getLogger(TransformationNetProcessingContainer.class);

	public TransformationNetProcessingContainer(ISpongeNet<ITransformer<?, ?>> net,
			ITokenStore<MessageContainer> initialState) {
		super(net, initialState, new MessageContainerFactory());

		onTokenTransition(e -> {
			ITokenTransitionOperation<MessageContainer> op = e.getSourceOperation();
			IToken transitionedToken = op.getTransitionedToken();
			String targetNodeId = op.getTargetNodeId();
			ITransformer<?, ?> transformer = getNet().getNode(targetNodeId);
			if (!(transformer instanceof IMerger)) {
				handleTransformation(transformer, (IMessageContainer) transitionedToken);
			}
		});

		onTokenCreation(e -> {
			ITokenCreationOperation<MessageContainer> op = e.getSourceOperation();
			IToken transitionedToken = op.getCreatedToken();
			String targetNodeId = op.getTargetNodeId();
			ITransformer<?, ?> transformer = getNet().getNode(targetNodeId);
			handleTransformation(transformer, (IMessageContainer) transitionedToken);
		});

		onTokenMerge(e -> {
			ITokenMergeOperation<MessageContainer> op = e.getSourceOperation();
			IToken resultToken = op.getResultToken();
			String targetNodeId = op.getTargetNodeId();
			IMerger<?> merger = (IMerger<?>) getNet().getNode(targetNodeId);
			handleTransformation(merger, (IMessageContainer) resultToken);
		});

		onStepStart(e -> {
			LOG.debug("Processing container %s, step %s with state: %s", "this", e.getStep(),
					ITokenStore.print(getStateContainer().getCurrentState()));
		});

	}

	public TransformationNetProcessingContainer(ISpongeNet<ITransformer<?, ?>> net) {
		this(net, new SimpleTokenStore<>());
	}

	@Override
	protected boolean isSplitterNode(ITransformer<?, ?> node) {
		return node.isAutoSplit();
	}

	@Override
	protected boolean isMergerNode(ITransformer<?, ?> node) {
		return node instanceof IMerger;
	}

	protected void handleTransformation(ITransformer<?, ?> node, IMessageContainer messageContainer) {
		IMessage<?> transformedMessage = node.transform(messageContainer.getMessage());
		((MessageContainer) messageContainer).setMessage(transformedMessage);
	}

	public List<MessageContainer> getAllCurrentlyProcessed() throws InterruptedException {
		
		return new ArrayList<>(getCurrentState().getTokens());
	}

}

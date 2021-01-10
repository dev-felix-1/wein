package de.fekl.wein.api.core;

import de.fekl.dine.api.state.SimpleTokenStore;
import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.sepe.ColouredNetProcessingContainer;

public class TransformationNetProcessor {

	private final ColouredNetProcessingContainer processingContainer;

	public TransformationNetProcessor(ISpongeNet transformationNet) {
		processingContainer = new ColouredNetProcessingContainer(transformationNet, new SimpleTokenStore());
	}

	public <T> void process(IMessage<T> message) {
		processingContainer.process(message);
	}

}

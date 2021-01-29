package de.fekl.stat.test.integration.bpmn;

import java.util.Map;

import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.stat.core.api.events.ITokenTransitionEvent;
import de.fekl.stat.core.impl.state.net.AsyncColouredNetProcessingContainer;

public class BpmnEngine {

	public void startProcess(ISpongeNet<IBpmnFlowObject> net, Map<String, Object> args) {
		var bpmnTokenFactory = new BpmnTokenFactory();
		var processingContainer = new AsyncColouredNetProcessingContainer<>(net, bpmnTokenFactory);

		processingContainer.onStateChangedEvent(e -> {
			if (e instanceof ITokenTransitionEvent) {
				@SuppressWarnings("unchecked")
				var sourceOperation = ((ITokenTransitionEvent<IBpmnToken>) (ITokenTransitionEvent) e)
						.getSourceOperation();
				IBpmnFlowObject node = net.getNode(sourceOperation.getSourceNodeId());
				if (node instanceof IBpmnActivity) {
					IBpmnToken transitionedToken = sourceOperation.getTransitionedToken();
					((IBpmnActivity) node).performActivity(transitionedToken);
				}
			}
		});

		IBpmnToken token = bpmnTokenFactory.createToken();
		args.entrySet().forEach(e -> {
			token.set(e.getKey(), e.getValue());
		});
		processingContainer.process(token);
	}

}

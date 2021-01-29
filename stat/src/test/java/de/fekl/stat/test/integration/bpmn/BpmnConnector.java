package de.fekl.stat.test.integration.bpmn;

import de.fekl.stat.core.api.edge.conditional.ICondition;
import de.fekl.stat.core.impl.edge.conditional.SimpleConditionalEdge;

public class BpmnConnector extends SimpleConditionalEdge<IBpmnFlowObject, IBpmnToken> implements IBpmnConnector {

	private final String label;

	public BpmnConnector(String label, String source, String target,
			ICondition<IBpmnFlowObject, IBpmnToken> condition) {
		super(source, target, condition);
		this.label = label;
	}

	@Override
	public String getId() {
		return label;
	}

}

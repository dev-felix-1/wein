package de.fekl.stat.test.integration.bpmn;

import de.fekl.dine.core.impl.node.SimpleNode;

public class BpmnActivity extends SimpleNode implements IBpmnActivity {

	public BpmnActivity(String id) {
		super(id);
	}

	@Override
	public void performActivity(IBpmnToken token) {
		System.err.println("PERFROM: " + token);
	}

}

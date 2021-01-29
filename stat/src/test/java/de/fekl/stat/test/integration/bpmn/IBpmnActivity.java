package de.fekl.stat.test.integration.bpmn;

public interface IBpmnActivity extends IBpmnFlowObject {

	void performActivity(IBpmnToken token);

}

package de.fekl.stat.test.integration.bpmn;

import de.fekl.dine.core.api.edge.ILabeledEdge;
import de.fekl.stat.core.api.edge.conditional.IConditionalEdge;

public interface IBpmnConnector extends IConditionalEdge<IBpmnFlowObject, IBpmnToken>, ILabeledEdge {

}

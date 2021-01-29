package de.fekl.stat.test.integration.bpmn;

import de.fekl.stat.core.api.node.IAutoMergeNode;
import de.fekl.stat.core.api.node.IAutoSplitNode;

/*
 * AND split equals stat-splitting without conditional edges 
 * AND merge equals stat-merge 
 */
public interface IBpmnGatewayAND extends IBpmnGateway, IAutoSplitNode, IAutoMergeNode {

}

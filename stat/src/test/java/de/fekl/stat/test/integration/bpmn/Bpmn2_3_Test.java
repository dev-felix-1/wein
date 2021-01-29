package de.fekl.stat.test.integration.bpmn;

import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.graph.DirectedGraphBuilder;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.dine.core.api.sponge.SpongeNetBuilder;

public class Bpmn2_3_Test {

	private ISpongeNet<IBpmnFlowObject> buildModel() {
		//@formatter:off
		ISpongeNet<IBpmnFlowObject> model = new SpongeNetBuilder<IBpmnFlowObject>()
				.setGraph(new DirectedGraphBuilder<IBpmnFlowObject>()
					.addNode(new BpmnEvent("Hunger festgestellt"))
					.addEdge("Hunger festgestellt", "Rezept aussuchen")
					
					.addNode(new BpmnActivity("Rezept aussuchen"))	
					.addEdge("Rezept aussuchen", "Gewünschtes Gericht?")
					
					.addNode(new BpmnGatewayXOR("Gewünschtes Gericht?"))	
					.addEdge(new BpmnConnector("Pasta", "Gewünschtes Gericht?", "Pasta kochen", (source,target,token)->
						token.get("gewuenschtes_gericht").equals("Pasta")
					))
					.addEdge(new BpmnConnector("Steak", "Gewünschtes Gericht?", "Steak braten", (source,target,token)->
						token.get("gewuenschtes_gericht").equals("Steak")
					))
					.addEdge(new BpmnConnector("Salat", "Gewünschtes Gericht?", "Salat anrichten", (source,target,token)->
						token.get("gewuenschtes_gericht").equals("Salat")
					))
					
					.addNode(new BpmnActivity("Pasta kochen"))	
					.addEdge("Pasta kochen", "Pasta fertig")
					
					.addNode(new BpmnActivity("Steak braten"))
					.addEdge("Steak braten", "Steak fertig")
					
					.addNode(new BpmnActivity("Salat anrichten"))	
					.addEdge("Salat anrichten", "Salat fertig")
					
					.addNode(new BpmnEvent("Pasta fertig"))	
					.addNode(new BpmnEvent("Steak fertig"))	
					.addNode(new BpmnEvent("Salat fertig"))	
				)
				.build();
		//@formatter:on
		return model;

	}

	@Test
	public void test() {
		buildModel();
	}

}

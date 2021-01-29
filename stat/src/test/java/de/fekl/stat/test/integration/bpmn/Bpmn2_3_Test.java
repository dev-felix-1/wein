package de.fekl.stat.test.integration.bpmn;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.graph.DirectedGraphBuilder;
import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.dine.core.api.sponge.SpongeNetBuilder;

public class Bpmn2_3_Test {

	private static final String LBL_SALAT_ANR = "Salat anrichten";
	private static final String LABEL_STEAK_BRATEN = "Steak braten";
	private static final String LBL_PASTA_KOCHEN = "Pasta kochen";
	private static final String SALAT = "Salat";
	private static final String STEAK = "Steak";
	private static final String PASTA = "Pasta";
	private static final String LBL_GEW_GER = "Gewünschtes Gericht?";
	private static final String VAR_GEW_GER = "gewuenschtes_gericht";

	private ISpongeNet<IBpmnFlowObject> buildModel() {
		//@formatter:off
		ISpongeNet<IBpmnFlowObject> model = new SpongeNetBuilder<IBpmnFlowObject>()
				.setGraph(new DirectedGraphBuilder<IBpmnFlowObject>()
					.addEdge(new BpmnEvent("Hunger festgestellt"), new BpmnActivity("Rezept aussuchen"))
					
					.addEdge("Rezept aussuchen", LBL_GEW_GER)
					
					.addNode(new BpmnGatewayXOR(LBL_GEW_GER))	
					.addEdge(new BpmnConnector(PASTA, LBL_GEW_GER, LBL_PASTA_KOCHEN, (source,target,token)->
						token.get(VAR_GEW_GER).equals(PASTA)
					))
					.addEdge(new BpmnConnector(STEAK, LBL_GEW_GER, LABEL_STEAK_BRATEN, (source,target,token)->
						token.get(VAR_GEW_GER).equals(STEAK)
					))
					.addEdge(new BpmnConnector(SALAT, LBL_GEW_GER, LBL_SALAT_ANR, (source,target,token)->
						token.get(VAR_GEW_GER).equals(SALAT)
					))
					
					.addNode(new BpmnActivity(LBL_PASTA_KOCHEN))	
					.addEdge(LBL_PASTA_KOCHEN, new BpmnEvent("Pasta fertig"))
					
					.addNode(new BpmnActivity(LABEL_STEAK_BRATEN))
					.addEdge(LABEL_STEAK_BRATEN, new BpmnEvent("Steak fertig"))
					
					.addNode(new BpmnActivity(LBL_SALAT_ANR))	
					.addEdge(LBL_SALAT_ANR, new BpmnEvent("Salat fertig"))
				)
				.build();
		//@formatter:on
		return model;

	}

	@Test
	public void test() {
		ISpongeNet<IBpmnFlowObject> buildModel = buildModel();
		Assertions.assertEquals("Hunger festgestellt", buildModel.getRoot().getId());
	}

}

package de.fekl.stat.test.integration.bpmn;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.dine.core.api.sponge.SpongeNetBuilder;
import de.fekl.stat.core.api.edge.conditional.IConditionEvaluationContext;

public class Bpmn2_05_Test {

	private static final String LBL_SALAT_ANR = "Salat anrichten";
	private static final String LBL_STEAK_BRATEN = "Steak braten";
	private static final String LBL_PASTA_KOCHEN = "Pasta kochen";
	private static final String LBL_GEW_GER = "Gewünschtes Gericht?";

	private ISpongeNet<IBpmnFlowObject> buildModel() {
		return new SpongeNetBuilder<IBpmnFlowObject>()
		//@formatter:off
				.addEdge(new BpmnEvent("Hunger festgestellt"), new BpmnActivity("Rezept aussuchen"))
				
				.addEdge("Rezept aussuchen", LBL_GEW_GER)
				
				.addNode(new BpmnGatewayXOR(LBL_GEW_GER))	
				.addEdge(new BpmnConnector("Pasta", LBL_GEW_GER, LBL_PASTA_KOCHEN, (context)-> gewuenscht(context)))
				.addEdge(new BpmnConnector("Steak", LBL_GEW_GER, LBL_STEAK_BRATEN, (context)-> gewuenscht(context)))
				.addEdge(new BpmnConnector("Salat", LBL_GEW_GER, LBL_SALAT_ANR, (context)-> gewuenscht(context)))
				
				.addNode(new BpmnActivity(LBL_PASTA_KOCHEN))	
				.addEdge(LBL_PASTA_KOCHEN, "Mahlzeit verzehren")
				
				.addNode(new BpmnActivity(LBL_STEAK_BRATEN))
				.addEdge(LBL_STEAK_BRATEN, "Mahlzeit verzehren")
				
				.addNode(new BpmnActivity(LBL_SALAT_ANR))	
				.addEdge(LBL_SALAT_ANR, "Mahlzeit verzehren")
				
				.addEdge(new BpmnActivity("Mahlzeit verzehren"), new BpmnEvent("Hunger gestillt"))
		//@formatter:on
				.build();
	}

	private boolean gewuenscht(IConditionEvaluationContext<IBpmnFlowObject, IBpmnToken> context) {
		return getGewuenschtesGericht(context.getToken()).equals(((BpmnConnector) context.getEdge()).getId());
	}

	private Object getGewuenschtesGericht(IBpmnToken token) {
		return token.get("gewuenschtes_gericht");
	}

	@Test
	public void test() {
		ISpongeNet<IBpmnFlowObject> buildModel = buildModel();
		Assertions.assertEquals("Hunger festgestellt", buildModel.getRoot().getId());

		new BpmnEngine().startProcess(buildModel, Collections.singletonMap("gewuenschtes_gericht", "Steak"));
	}

}

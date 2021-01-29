package de.fekl.stat.test.integration.bpmn;

import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.core.api.sponge.ISpongeNet;
import de.fekl.dine.core.api.sponge.SpongeNetBuilder;
import de.fekl.stat.core.api.edge.conditional.IConditionEvaluationContext;

public class Bpmn2_11_Test {

	private ISpongeNet<IBpmnFlowObject> buildModel() {
		return new SpongeNetBuilder<IBpmnFlowObject>()
		//@formatter:off
				.chain( new BpmnEvent		("Hunger festgestellt"), 
						new BpmnActivity	("Rezept aussuchen"),
						new BpmnGatewayAND	("Betrachte beides:"))
				
				.fork(  "Betrachte beides:", 
							new BpmnGatewayXOR	("Etwas ordentliches gewünscht?"), 
							new BpmnGatewayXOR	("Salat gewünscht?"))
				
				.chain( new BpmnGatewayXOR	("Etwas ordentliches fertig"), 
						new BpmnGatewayXOR	("Etwas ordentliches fertig oder nicht"),
						new BpmnGatewayAND	("Essen fertig"),
						new BpmnActivity	("Mahlzeit verzehren"), 
						new BpmnEvent		("Hunger gestillt"))
				
				.addNode(new BpmnGatewayXOR	("Gewünschtes Gericht?"))
				
				.addEdge(new BpmnConnector("Ja", "Etwas ordentliches gewünscht?", "Gewünschtes Gericht?", this::isEtwasOrdentlichesGewuenscht))
				.addEdge(new BpmnConnector("Nein", "Etwas ordentliches gewünscht?", "Etwas ordentliches fertig oder nicht", this::isEtwasOrdentlichesGewuenscht))
				
				.addEdge(new BpmnActivity("Salat anrichten"), new BpmnGatewayXOR("Salat fertig oder nicht"))
				
				.addEdge(new BpmnConnector("Ja", "Salat gewünscht?", "Salat anrichten", this::isSalatGewuenscht))
				.addEdge(new BpmnConnector("Nein", "Salat gewünscht?", "Salat fertig oder nicht", this::isSalatGewuenscht))
				
				.addEdge("Salat fertig oder nicht","Essen fertig")
				
				.join(new BpmnActivity("Pasta kochen"), new BpmnActivity("Steak braten")).on("Etwas ordentliches fertig")
				
				.addEdge(new BpmnConnector("Pasta", "Gewünschtes Gericht?", "Pasta kochen", this::isGewuenschtesGericht))
				.addEdge(new BpmnConnector("Steak", "Gewünschtes Gericht?", "Steak braten", this::isGewuenschtesGericht))
				
				
		//@formatter:on
				.build();
	}

	private boolean isGewuenschtesGericht(IConditionEvaluationContext<IBpmnFlowObject, IBpmnToken> context) {
		return context.getToken().get("gewuenschtes_gericht").toString()
				.equalsIgnoreCase(((BpmnConnector) context.getEdge()).getId());
	}

	private boolean isEtwasOrdentlichesGewuenscht(IConditionEvaluationContext<IBpmnFlowObject, IBpmnToken> context) {
		return context.getToken().get("etwas_ordentliches_gewuenscht").toString()
				.equalsIgnoreCase(((BpmnConnector) context.getEdge()).getId());
	}

	private boolean isSalatGewuenscht(IConditionEvaluationContext<IBpmnFlowObject, IBpmnToken> context) {
		return context.getToken().get("salat_gewuenscht").toString()
				.equalsIgnoreCase(((BpmnConnector) context.getEdge()).getId());
	}

	@Test
	public void test() {
		ISpongeNet<IBpmnFlowObject> buildModel = buildModel();
		Assertions.assertEquals("Hunger festgestellt", buildModel.getRoot().getId());

		new BpmnEngine().startProcess(buildModel, new HashMap<String, Object>() {
			{
				put("gewuenschtes_gericht", "Steak");
				put("etwas_ordentliches_gewuenscht", "Ja");
				put("salat_gewuenscht", "Nein");
			}
		});
	}
	

}

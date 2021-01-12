package de.fekl.tone.api.core.x;

import org.codehaus.groovy.control.messages.SimpleMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.api.graph.DirectedGraphBuilder;
import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.dine.api.tree.SpongeNetBuilder;
import de.fekl.esta.api.core.IStateHasChangedEvent;
import de.fekl.esta.api.core.SimpleStateContainer;
import de.fekl.tran.IMessage;
import de.fekl.tran.ITransformer;
import de.fekl.tran.SimpleMessageFactory;
import de.fekl.tran.SimpleTransformationRoute;
import de.fekl.tran.SimpleTransformer;
import de.fekl.tran.SimpleTransformerFactoryParams;
import de.fekl.tran.StandardContentTypes;
import de.fekl.tran.TransformationNetProcessingContainer;
import de.fekl.tran.TransformationRouteProcessor;
import de.fekl.tran.TransformerBuilder;

public class TranTest {

	private static final String A = "A";
	private static final String B = "B";
	private static final String C = "C";

	private static ISpongeNet<ITransformer> createSimpleABCNet() {
		TransformerBuilder<String, String> transformerBuilder = new TransformerBuilder<String, String>()
				.source(StandardContentTypes.PRETTY_XML_STRING).target(StandardContentTypes.PRETTY_XML_STRING);

		return new SpongeNetBuilder<ITransformer>()
		//@formatter:off
				.setGraph(new DirectedGraphBuilder<ITransformer>()
					.addNode(transformerBuilder
								.name(A)
								.transformation(o->o+A)
								.build())
					.addNode(transformerBuilder
								.name(B)
								.transformation(o->o+B)
								.build())
					.addNode(transformerBuilder
								.name(C)
								.transformation(o->o+C)
								.build())
					.addEdge(A, B)
					.addEdge(B, C)
					.build())
				//@formatter:on
				.setStartNode(A).build();
	}
	
	private static ISpongeNet<ITransformer> createSimpleABCNet2() {
		TransformerBuilder<String, String> transformerBuilder = new TransformerBuilder<String, String>()
				.source(StandardContentTypes.PRETTY_XML_STRING).target(StandardContentTypes.PRETTY_XML_STRING);
		
		return new SpongeNetBuilder<ITransformer>()
				//@formatter:off
				.setGraph(new DirectedGraphBuilder<ITransformer<?,?>>()
						.addNode(new SimpleTransformerFactoryParams(
								A,
								StandardContentTypes.PRETTY_XML_STRING,
								StandardContentTypes.PRETTY_XML_STRING,
								o->o+A
								))
						.addNode(transformerBuilder
								.name(B)
								.transformation(o->o+B)
								.build())
						.addNode(transformerBuilder
								.name(C)
								.transformation(o->o+C)
								.build())
						.addEdge(A, B)
						.addEdge(B, C)
						.build())
				//@formatter:on
				.setStartNode(A).build();
	}

	@Test
	public void testTransformationRouteProcessor() throws InterruptedException {
		ISpongeNet<ITransformer> transformerNet = createSimpleABCNet();
		SimpleTransformationRoute<String, String> simpleTransformationRoute = new SimpleTransformationRoute<>(
				StandardContentTypes.PRETTY_XML_STRING, StandardContentTypes.PRETTY_XML_STRING, transformerNet);

		TransformationRouteProcessor transformationRouteProcessor = new TransformationRouteProcessor();
		IMessage<String> message = new SimpleMessageFactory().createMessage("hello");

		IMessage<String> processed = transformationRouteProcessor.process(message, simpleTransformationRoute);
		System.err.println(processed);

	}

}

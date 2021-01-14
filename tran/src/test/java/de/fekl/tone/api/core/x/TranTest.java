package de.fekl.tone.api.core.x;

import org.junit.jupiter.api.Test;

import de.fekl.dine.api.graph.DirectedGraphBuilder;
import de.fekl.dine.api.tree.ISpongeNet;
import de.fekl.dine.api.tree.SpongeNetBuilder;
import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.ITransformationRoute;
import de.fekl.tran.api.core.ITransformer;
import de.fekl.tran.impl.SimpleMessageFactory;
import de.fekl.tran.impl.SimpleTransformationRoute;
import de.fekl.tran.impl.SimpleTransformerRegistry;
import de.fekl.tran.impl.StandardContentTypes;
import de.fekl.tran.impl.TransformationRouteBuilder;
import de.fekl.tran.impl.TransformationRouteProcessor;
import de.fekl.tran.impl.TransformerBuilder;

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
								.id(A)
								.transformation(o->o+A)
								.build())
					.addNode(transformerBuilder
								.id(B)
								.transformation(o->o+B)
								.build())
					.addNode(transformerBuilder
								.id(C)
								.transformation(o->o+C)
								.build())
					.addEdge(A, B)
					.addEdge(B, C)
					.build())
				//@formatter:on
				.setStartNode(A).build();
	}

	@SuppressWarnings("unchecked")
	private static ISpongeNet<ITransformer> createSimpleABCNet2() {
		TransformerBuilder<String, String> transformerBuilder = new TransformerBuilder<String, String>()
				.source(StandardContentTypes.PRETTY_XML_STRING).target(StandardContentTypes.PRETTY_XML_STRING);

		return new SpongeNetBuilder<ITransformer>()
		//@formatter:off
				.setGraph(new DirectedGraphBuilder<ITransformer>()
						.setNodeBuilder(new TransformerBuilder()
								.source(StandardContentTypes.PRETTY_XML_STRING)
								.target(StandardContentTypes.PRETTY_XML_STRING))
						.addNode(A)
						.addNode(transformerBuilder
								.id(B)
								.transformation(o->o+B)
								.build())
						.addNode(transformerBuilder
								.id(C)
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
		ISpongeNet<ITransformer> transformerNet2 = createSimpleABCNet2();

		//@formatter:off
		ITransformationRoute<String, String> route = new TransformationRouteBuilder<String,String>(
				new SpongeNetBuilder<ITransformer>()
					.setGraph(new DirectedGraphBuilder<ITransformer>()
							.addNode(new TransformerBuilder<String,String>()
								.id(A)
								.source(StandardContentTypes.PRETTY_XML_STRING)
								.target(StandardContentTypes.PRETTY_XML_STRING)
								.transformation(o->o+A))
							.addNode(new TransformerBuilder<String,String>()
								.id(B)
								.source(StandardContentTypes.PRETTY_XML_STRING)
								.target(StandardContentTypes.PRETTY_XML_STRING)
								.transformation(o->o+B))
							.addNode(new TransformerBuilder<String,String>()
								.id(C)
								.source(StandardContentTypes.PRETTY_XML_STRING)
								.target(StandardContentTypes.PRETTY_XML_STRING)
								.transformation(o->o+C))
							.addEdge(A, B)
							.addEdge(B, C)
							.build())
					.setStartNode(A)
				).build();
							
		//@formatter:on

//		SimpleTransformationRoute<String, String> simpleTransformationRoute = new SimpleTransformationRoute<>(
//				StandardContentTypes.PRETTY_XML_STRING, StandardContentTypes.PRETTY_XML_STRING, transformerNet);

		TransformationRouteProcessor transformationRouteProcessor = new TransformationRouteProcessor();
		IMessage<String> message = new SimpleMessageFactory().createMessage("hello");

		IMessage<String> processed = transformationRouteProcessor.process(message, route);
		System.err.println(processed);

	}
	
	@Test
	public void testTransfomerRegistry() throws InterruptedException {
		SimpleTransformerRegistry transformerRegistry = new SimpleTransformerRegistry();
		//@formatter:off
		transformerRegistry.register(new TransformerBuilder<String,String>()
				.id(A)
				.source(StandardContentTypes.PRETTY_XML_STRING)
				.target(StandardContentTypes.PRETTY_XML_STRING)
				.transformation(o->o+A).build());
		transformerRegistry.register(new TransformerBuilder<String,String>()
				.id(B)
				.source(StandardContentTypes.PRETTY_XML_STRING)
				.target(StandardContentTypes.PRETTY_XML_STRING)
				.transformation(o->o+B).build());
		TransformerBuilder<String, String> fromRegistry = new TransformerBuilder<String,String>().setTransformerRegistry(transformerRegistry);
		ITransformationRoute<String, String> route = new TransformationRouteBuilder<String,String>(
				new SpongeNetBuilder<ITransformer>()
				.setGraph(new DirectedGraphBuilder<ITransformer>()
						.addNode(fromRegistry.id(A))
						.addNode(fromRegistry.id(B))
						.addNode(new TransformerBuilder<String,String>()
								.id(C)
								.source(StandardContentTypes.PRETTY_XML_STRING)
								.target(StandardContentTypes.PRETTY_XML_STRING)
								.transformation(o->o+C))
						.addEdge(A, B)
						.addEdge(B, C)
						.build())
				.setStartNode(A)
				).build();
		
		//@formatter:on
		
		TransformationRouteProcessor transformationRouteProcessor = new TransformationRouteProcessor();
		IMessage<String> message = new SimpleMessageFactory().createMessage("hello");
		
		IMessage<String> processed = transformationRouteProcessor.process(message, route);
		System.err.println(processed);
		
	}

}

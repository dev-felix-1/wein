package de.fekl.wein.groovy.support

import org.junit.jupiter.api.Test

import de.fekl.tran.api.core.IMessage
import de.fekl.tran.groovy.support.GRouteBuilder
import de.fekl.tran.groovy.support.GTransformerBuilder
import de.fekl.tran.impl.SimpleMessageFactory
import de.fekl.tran.impl.SimpleTransformerRegistry
import de.fekl.tran.impl.StandardContentTypes
import de.fekl.tran.impl.TransformationRouteBuilder
import de.fekl.tran.impl.TransformationRouteProcessor

class GRouteBuilderTest {

	@Test
	public void test1() {

		TransformationRouteBuilder b = new GRouteBuilder().route {
			nodes {
				node {
					id 'A'
					source StandardContentTypes.PRETTY_XML_STRING
					target StandardContentTypes.PRETTY_XML_STRING
				}
				node {
					id 'B'
					source StandardContentTypes.PRETTY_XML_STRING
					target StandardContentTypes.PRETTY_XML_STRING
				}
			}
			edges {
				edge (source: 'A', target: 'B')
			}
		}

		println	b.build();
	}

	@Test
	public void test2() {

		TransformationRouteBuilder b = new GRouteBuilder().route {
			nodes {
				node {
					id 'A'
					source StandardContentTypes.PRETTY_XML_STRING
					target StandardContentTypes.PRETTY_XML_STRING
				}
				node {
					id 'B'
					source StandardContentTypes.PRETTY_XML_STRING
					target StandardContentTypes.PRETTY_XML_STRING
				}
			}
		}

		println	b.build();
	}

	@Test
	public void test3() {

		TransformationRouteBuilder b = new GRouteBuilder().route {
			node {
				id 'A'
				'in/out' 'xml'
				transform { x -> x+'A' }
			}
			node {
				'in/out' 'xml'
				id 'B'
				transform { x -> x+'B' }
			}
		}

		def route = b.build();

		def processed = new TransformationRouteProcessor().process('hello', route);
		System.err.println(processed);
	}

	@Test
	public void test4() {

		TransformationRouteBuilder b = new GRouteBuilder().route {
			transformation (id: 'A', input: 'xml', output: 'xml') { x -> x+'A'}
			transformation (id: 'B', input: 'xml', output: 'xml') { x -> x+'B'}
		}

		def route = b.build();

		def processed = new TransformationRouteProcessor().process('hello', route);
		System.err.println(processed);
	}

	@Test
	public void test5() {

		TransformationRouteBuilder b = new GRouteBuilder().route {
			transformation ('i/o': 'xml') { x -> x+'A'}
			transformation ('i/o': 'xml') { x -> x+'B'}
		}

		def route = b.build();

		def processed = new TransformationRouteProcessor().process('hello', route);
		System.err.println(processed);
	}

	@Test
	public void test6() {
		def routeBuilder = new GRouteBuilder()

		def route1 = routeBuilder.route {
			transform ('xml') { it+'A' }
			transform ('xml') { it+'B' }
		}.build()

		def route2 = routeBuilder.route {
			transform ('xml') { it+'C' }
			transform ('xml') { it+'D' }
		}.build()

		def processed1 = new TransformationRouteProcessor().process('hello1', route1);
		def processed2 = new TransformationRouteProcessor().process('hello2', route2);
		System.err.println(processed1);
		System.err.println(processed2);
	}

	@Test
	public void test6_2() {
		def routeBuilder = new GRouteBuilder()

		def route1 = routeBuilder {
			transform ('xml') { it+'A' }
			transform ('xml') { it+'B' }
		}

		def route2 = routeBuilder {
			transform ('xml') { it+'C' }
			transform ('xml') { it+'D' }
		}

		def processed1 = new TransformationRouteProcessor().process('hello1', route1);
		def processed2 = new TransformationRouteProcessor().process('hello2', route2);
		System.err.println(processed1);
		System.err.println(processed2);
	}

	@Test
	public void test7() {
		def registry = new SimpleTransformerRegistry();
		def routeBuilder = new GRouteBuilder()
		routeBuilder.setTransformerRegistry(registry)
		def transformerBuilder = new GTransformerBuilder()

		def transformer1 = transformerBuilder.transformer (id: 'A', input: 'xml', output: 'xml') { x -> x+'A' }.build()

		registry.register(transformer1)
		def route1 = routeBuilder.route {
			node ('A')
			transform ('xml') { it+'B' }
		}.build()

		def processed1 = new TransformationRouteProcessor().process('hello1', route1);
		System.err.println(processed1);
	}

	@Test
	public void test8() {
		def registry = new SimpleTransformerRegistry();
		def routeBuilder = new GRouteBuilder()
		routeBuilder.setTransformerRegistry(registry)
		def transformerBuilder = new GTransformerBuilder()

		registry.register( transformerBuilder (id: 'A', input: 'xml', output: 'xml') { x -> x+'A' } )
		registry.register( transformerBuilder (id: 'B', input: 'xml', output: 'xml') { x -> x+'B' } )
		registry.register( transformerBuilder (id: 'C', input: 'xml', output: 'xml') { x -> x+'C' } )

		def route1 = routeBuilder .route ( nodes : ['A', 'B', 'C'])

		def processed1 = new TransformationRouteProcessor().process('hello1', route1);
		System.err.println(processed1);
	}

	@Test
	public void test9() {
		def registry = new SimpleTransformerRegistry();
		def routeBuilder = new GRouteBuilder()
		routeBuilder.setTransformerRegistry(registry)
		def transformerBuilder = new GTransformerBuilder()

		registry.register( transformerBuilder . transformer {
			id 'A'
			inputContentType 'string'
			outputContentType 'string'
			transformation { o -> o + 'A' }
		} .build() )
		registry.register( transformerBuilder {
			id 'B'
			inputContentType 'string'
			outputContentType 'string'
			transformation { o -> o + 'B' }
		} )
		registry.register( transformerBuilder (id: 'C', input: 'xml', output: 'xml') { x -> x+'C' } )

		def route1 = routeBuilder {
			id 'route1'
			nodes {
				A
				B
				C
			}
		}

		def processed1 = new TransformationRouteProcessor().process('hello1', route1);
		System.err.println(processed1);
	}
}

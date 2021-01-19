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
				edge {
					source 'A'
					target 'B'
				}
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
		def transformerBuilder = new GTransformerBuilder()

		routeBuilder.transformerRegistry = registry
		transformerBuilder.transformerRegistry = registry
		transformerBuilder.autoRegister = true

		transformerBuilder {
			id 'A'
			inputContentType StandardContentTypes.STRING
			outputContentType 'string'
			transformation { o -> o + 'A' }
		}
		transformerBuilder {
			id 'B'
			inputContentType 'string'
			outputContentType 'xml'
			transformation { o -> o + 'B' }
		}
		transformerBuilder (id: 'C', input: 'xml', output: 'xml') { x -> x+'C' }

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

	@Test
	public void test91() {

		def registry = new SimpleTransformerRegistry();
		def routeBuilder = new GRouteBuilder()
		def transformerBuilder = new GTransformerBuilder()

		routeBuilder.transformerRegistry = registry
		transformerBuilder.transformerRegistry = registry
		transformerBuilder.autoRegister = true

		transformerBuilder {
			id 'A'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'A' }
		}
		transformerBuilder {
			id 'B'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'B' }
		}
		transformerBuilder {
			id 'C'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'C' }
		}
		transformerBuilder {
			id 'D'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'D' }
		}

		def route1 = routeBuilder {
			id 'route1'
			nodes {
				A
				B
				C
				D
			}
			edges {
				edge (source: 'A', target: ['C', 'B'])
				edge (source: 'B', target: 'D')
				edge (source: 'C', target: 'D')
			}
		}

		def processed1 = new TransformationRouteProcessor().process('hello1', route1);
		System.err.println(processed1);
	}

	@Test
	public void test92() {

		def registry = new SimpleTransformerRegistry();
		def routeBuilder = new GRouteBuilder()
		def transformerBuilder = new GTransformerBuilder()

		routeBuilder.transformerRegistry = registry
		transformerBuilder.transformerRegistry = registry
		transformerBuilder.autoRegister = true

		transformerBuilder {
			id 'A'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'A' }
		}
		transformerBuilder {
			id 'B'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'B' }
		}
		transformerBuilder {
			id 'C'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'C' }
		}
		transformerBuilder {
			id 'D'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'D' }
		}
		transformerBuilder {
			id 'E'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'E' }
		}
		transformerBuilder {
			id 'F'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'F' }
		}
		transformerBuilder {
			id 'G'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'G' }
		}

		def route1 = routeBuilder {
			edges {
				edge ('A', ['B', 'C', 'D'])
				edge (['B', 'C'], 'E')
				edge ('D', 'F')
				edge (['E', 'F'], 'G')
			}
		}

		def processed1 = new TransformationRouteProcessor().process('hello1', route1);
		System.err.println(processed1);
	}

	@Test
	public void test93() {

		def registry = new SimpleTransformerRegistry();
		def routeBuilder = new GRouteBuilder()
		def transformerBuilder = new GTransformerBuilder()

		routeBuilder.transformerRegistry = registry
		transformerBuilder.transformerRegistry = registry
		transformerBuilder.autoRegister = true

		transformerBuilder {
			id 'A'
			autoSplit true
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'A' }
		}
		transformerBuilder {
			id 'B'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'B' }
		}
		transformerBuilder {
			id 'C'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'C' }
		}
		transformerBuilder {
			id 'D'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'D' }
		}
		transformerBuilder {
			id 'E'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'E' }
		}
		transformerBuilder {
			id 'F'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'F' }
		}
		transformerBuilder {
			id 'G'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'G' }
		}

		def route1 = routeBuilder {
			nodes {
				node('A')
				node('B')
				node('C')
				node('D')
				node('E')
				node('F')
				node('G')
			}
			edges {
				edge ('A', ['B', 'C', 'D'])
				edge (['B', 'C'], 'E')
				edge ('D', 'F')
				edge (['E', 'F'], 'G')
			}
		}

		def processed1 = new TransformationRouteProcessor().processForMultiResult('hello1', route1);
		System.err.println(processed1);
	}

	@Test
	public void test94() {

		def registry = new SimpleTransformerRegistry();
		def routeBuilder = new GRouteBuilder()
		def transformerBuilder = new GTransformerBuilder()

		routeBuilder.transformerRegistry = registry
		transformerBuilder.transformerRegistry = registry
		transformerBuilder.autoRegister = true

		transformerBuilder {
			id 'A'
			autoSplit true
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'A' }
		}
		transformerBuilder {
			id 'B'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'B' }
		}
		transformerBuilder {
			id 'C'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'C' }
		}
		transformerBuilder {
			autoSplit true
			id 'D'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'D' }
		}
		transformerBuilder {
			id 'E'
			inputContentType StandardContentTypes.STRING
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			merge { o -> o?.join(', ') }
		}
		transformerBuilder {
			id 'F'
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + 'F' }
		}
		transformerBuilder {
			id 'G'
			inputContentType StandardContentTypes.STRING
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			merge { o -> o?.join(', ') }
		}

		def route1 = routeBuilder {
			nodes {
				node('A')
				node('B')
				node('C')
				node('D')
				node('E')
				node('F')
				node('G')
			}
			edges {
				edge ('A', ['B', 'C', 'D'])
				edge (['B', 'C'], 'E')
				edge ('D', 'F')
				edge (['E', 'F'], 'G')
			}
		}

		def processed1 = new TransformationRouteProcessor().processForMultiResult('hello1', route1);
		System.err.println(processed1);
	}

	@Test
	public void test95_Performance_1() {
		def registry = new SimpleTransformerRegistry();
		def routeBuilder = new GRouteBuilder()
		def transformerBuilder = new GTransformerBuilder()

		routeBuilder.transformerRegistry = registry
		transformerBuilder.transformerRegistry = registry
		transformerBuilder.autoRegister = true

		transformerBuilder {
			id "Node0".toString()
			autoSplit true
			inputContentType StandardContentTypes.STRING
			outputContentType StandardContentTypes.STRING
			transformation { o -> o + "_0" }
		}
		for (int i = 0 ; i < 10 ; i++) {
			def si = i
			transformerBuilder {
				id "Node0$i".toString()
				autoSplit true
				inputContentType StandardContentTypes.STRING
				outputContentType StandardContentTypes.STRING
				transformation { o -> o + "_$si" }
			}
		}
		for (int i = 10 ; i < 100 ; i++) {
			def si = i
			transformerBuilder {
				id "Node$i".toString()
				autoSplit true
				inputContentType StandardContentTypes.STRING
				outputContentType StandardContentTypes.STRING
				transformation { o -> o + "_$si" }
			}
		}

		transformerBuilder {
			id "Node100".toString()
			for(int i = 1 ; i < 10 ; i++) {
				inputContentType StandardContentTypes.STRING
			}
			outputContentType StandardContentTypes.STRING
			autoSplit true
			merge { o -> o?.join(', ') }
		}

		def route1 = routeBuilder {
			edges {
				for(int i = 1 ; i < 10 ; i++) {
					edge('Node0',"Node0${i}")
				}
				for(int i = 0 ; i < 9 ; i++) {
					for (int j = 1 ; j < 10 ; j++) {
						edge ("Node$i$j","Node${i+1}${j}")
					}
				}
				for (int j = 1 ; j < 10 ; j++) {
					edge ("Node9$j","Node100")
				}
			}
		}

		def processed1 = new TransformationRouteProcessor().processForMultiResult('hello', route1);
		System.err.println(processed1);
	}
	@Test
	public void test95_Performance_2() {
		int numberOfRoutes = 100
		int numberOfTransformers = 100
		
		def registry = new SimpleTransformerRegistry();
		def routeBuilder = new GRouteBuilder()
		def transformerBuilder = new GTransformerBuilder()

		routeBuilder.transformerRegistry = registry
		transformerBuilder.transformerRegistry = registry
		transformerBuilder.autoRegister = true

		for (int i = 0 ; i < numberOfTransformers ; i++) {
			def si = i
			transformerBuilder {
				id "Node$i".toString()
				inputContentType StandardContentTypes.INTEGER
				outputContentType StandardContentTypes.INTEGER
				transformation { o -> o!=null?o + 1:1 }
			}
		}

		for(int a = 0; a < numberOfRoutes ; a++) {
			def route1 = routeBuilder {
				edges {
					for(int i = 1 ; i < numberOfTransformers -1 ; i++) {
						edge("Node${i}","Node${i+1}")
					}
				}
			}
			def processed1 = new TransformationRouteProcessor().process(0, route1);
		}
	}
}

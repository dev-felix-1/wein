package de.fekl.wein.groovy.support

import org.junit.jupiter.api.Test

import de.fekl.tran.api.core.IMessage
import de.fekl.tran.groovy.support.GRouteBuilder
import de.fekl.tran.impl.SimpleMessageFactory
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
		
		TransformationRouteBuilder b = new GRouteBuilder().route {
			transform ('xml') { it+'A'}
			transform ('xml') { it+'B'}
		}
		
		def route = b.build();
		
		def processed = new TransformationRouteProcessor().process('hello', route);
		System.err.println(processed);
	}
}

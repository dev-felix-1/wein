package de.fekl.wein.groovy.support

import de.fekl.tran.groovy.support.GRouteBuilder
import de.fekl.tran.groovy.support.GTransformerBuilder
import de.fekl.tran.impl.SimpleTransformerRegistry
import de.fekl.tran.impl.StandardContentTypes
import de.fekl.tran.impl.TransformationRouteProcessor

class MainTest {
	
	 static void main(String[]args) {
		 
		def reader = System.'in'.newReader()
		
		reader.readLine()
		
		def registry = new SimpleTransformerRegistry();
		def routeBuilder = new GRouteBuilder()
		def transformerBuilder = new GTransformerBuilder()

		routeBuilder.transformerRegistry = registry
		transformerBuilder.transformerRegistry = registry
		transformerBuilder.autoRegister = true

		for (int i = 0 ; i < 1000 ; i++) {
			def si = i
			transformerBuilder {
				id "Node$i".toString()
				inputContentType StandardContentTypes.INTEGER
				outputContentType StandardContentTypes.INTEGER
				transformation { o -> o!=null?o + 1:1 }
			}
		}

		def route1 = routeBuilder {
			edges {
				for(int i = 1 ; i < 999 ; i++) {
					edge("Node${i}","Node${i+1}")
				}
			}
		}

		def processed1 = new TransformationRouteProcessor().process(0, route1);
		System.err.println(processed1);

		reader.readLine()		
	}
}

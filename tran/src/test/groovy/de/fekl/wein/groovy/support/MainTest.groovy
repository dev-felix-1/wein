package de.fekl.wein.groovy.support

import de.fekl.tran.groovy.support.GRouteBuilder
import de.fekl.tran.groovy.support.GTransformerBuilder
import de.fekl.tran.impl.SimpleTransformerRegistry
import de.fekl.tran.impl.StandardContentTypes
import de.fekl.tran.impl.TransformationRouteProcessor
import groovy.transform.CompileStatic


class MainTest {

	static void main(String[]args) {

		def reader = System.'in'.newReader()

		reader.readLine()


		int initialSplit = 60
		int numberOfRoutes = 600
		int numberOfTransformers = 500
		int addRandomEdgesNumber = 110

		def registry = new SimpleTransformerRegistry();
		def routeBuilder = new GRouteBuilder()
		def transformerBuilder = new GTransformerBuilder()

		routeBuilder.transformerRegistry = registry
		transformerBuilder.transformerRegistry = registry
		transformerBuilder.autoRegister = true

		transformerBuilder {
			autoSplit true
			id "Node0".toString()
			inputContentType StandardContentTypes.INTEGER
			outputContentType StandardContentTypes.INTEGER
			transformation { o -> o }
		}

		for (int i = 1 ; i < numberOfTransformers -1 ; i++) {
			def si = i
			transformerBuilder {
				id "Node$i".toString()
				inputContentType StandardContentTypes.INTEGER
				outputContentType StandardContentTypes.INTEGER
				transformation { o -> o!=null?o + 1:1 }
			}
		}

		if (addRandomEdgesNumber < 1) {
			transformerBuilder {
				id "Node${numberOfTransformers-1}".toString()
				for(int i = 0; i < initialSplit; i++) {
					inputContentType StandardContentTypes.INTEGER
				}
				outputContentType StandardContentTypes.INTEGER
				merge { o -> o?.sum() }
			}
		} else {
			transformerBuilder {
				id "Node${numberOfTransformers-1}".toString()
				inputContentType StandardContentTypes.INTEGER
				outputContentType StandardContentTypes.INTEGER
				transformation { o -> o }
			}
		}

		for(int a = 0; a < numberOfRoutes ; a++) {
			for(int i = 1 ; i < initialSplit + 1 ; i++) {
				println (["Node0", "Node${i}"])
				for (int j = 0; i+(initialSplit*(j+1)) < numberOfTransformers - 1; j++) {
					println ([
						"Node${i+(initialSplit*j)}",
						"Node${i+(initialSplit*(j+1))}"
					])
				}
			}
			for(int i = numberOfTransformers - 1 - initialSplit ; i < numberOfTransformers - 1 ; i++) {
				println ([
					"Node${i}",
					"Node${numberOfTransformers-1}"
				])
			}
			def route1 = routeBuilder {
				edges {
					for(int i = 1 ; i < initialSplit + 1 ; i++) {
						edge ("Node0","Node${i}")
						for (int j = 0; i+(initialSplit*(j+1)) < numberOfTransformers - 1; j++) {
							edge ("Node${i+(initialSplit*j)}","Node${i+(initialSplit*(j+1))}")
						}
					}
					for(int i = numberOfTransformers - 1 - initialSplit ; i < numberOfTransformers - 1 ; i++) {
						edge ("Node${i}","Node${numberOfTransformers-1}")
					}
					for(int i = 0 ; i < addRandomEdgesNumber ; i++) {
						def nodeA = Math.random()*(numberOfTransformers-2)
						def nodeB = Math.random()*(numberOfTransformers-2)
						while (nodeA as int == nodeB as int) {
							nodeA = Math.random()*(numberOfTransformers-2)
						}
						println ([
							"Node${Math.min(nodeA, nodeB) as int}",
							"Node${Math.max(nodeA, nodeB) as int}"
						])
						edge ("Node${Math.min(nodeA, nodeB) as int}","Node${Math.max(nodeA, nodeB) as int}")
					}
				}
			}
			if (addRandomEdgesNumber < 1) {
				def processed1 = new TransformationRouteProcessor().process(0, route1);
				println processed1
			} else {
				def processed1 = new TransformationRouteProcessor().processForMultiResult(0, route1);
				println processed1
			}
		}
		println "juhu"
		reader.readLine()
	}
}

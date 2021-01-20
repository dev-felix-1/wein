package de.fekl.wein.groovy.support

import de.fekl.dine.api.graph.DirectedGraphBuilder
import de.fekl.dine.api.tree.SpongeNetBuilder
import de.fekl.tran.api.core.ITransformationRoute
import de.fekl.tran.api.core.ITransformerRegistry
import de.fekl.tran.impl.TransformationRouteBuilder
import de.fekl.tran.impl.TransformerBuilder
import de.fekl.tran.impl.TransformerNames
import de.fekl.wein.groovy.support.GModuleBuilder.CompositeEdgesBuilder
import de.fekl.wein.groovy.support.GModuleBuilder.CompositeNodeBuilder
import de.fekl.wein.groovy.support.GModuleBuilder.CompositeNodesBuilder
import groovy.transform.CompileDynamic

class GModuleBuilderRouteBuilder extends TransformationRouteBuilder {
	CompositeNodesBuilder nodesBuilder
	List<CompositeNodeBuilder> nodeBuilders = []
	CompositeEdgesBuilder edgesBuilder
	ITransformerRegistry transformerRegistry
	

	@CompileDynamic
	@Override
	public ITransformationRoute build() {
		def spongeNetBuilder = new SpongeNetBuilder()
		def graphBuilder = new DirectedGraphBuilder();

		if (!nodesBuilder) {
			nodesBuilder = new CompositeNodesBuilder()
		}
		nodesBuilder._nodeBuilders += nodeBuilders

		nodesBuilder._nodeBuilders.each { nodeBuilder ->
			def transformerBuilder = new TransformerBuilder()
			if (transformerRegistry) {
				transformerBuilder.setRegistry(transformerRegistry)
			}
			if (!nodeBuilder.id) {
				nodeBuilder.id = TransformerNames.generateTransformerName()
			}
			graphBuilder.addNode(transformerBuilder
					.id(nodeBuilder.id)
					.source(nodeBuilder.source)
					.target(nodeBuilder.target)
					.transformation(nodeBuilder.transformation))
		}
		if (edgesBuilder) {
			edgesBuilder.edgeBuilders.each {graphBuilder.addEdge(it.source,it.target)}
		} else if(nodesBuilder._nodeBuilders.size()>1){
			for (int i = 0; i<nodesBuilder._nodeBuilders.size()-1;i++) {
				graphBuilder.addEdge(nodesBuilder._nodeBuilders[i].id,nodesBuilder._nodeBuilders[i+1].id)
			}
		}
		spongeNetBuilder.setStartNode(nodesBuilder._nodeBuilders.first().id)
		spongeNetBuilder.setGraph(graphBuilder)
		super.transformation(spongeNetBuilder)
		return super.build();
	}
}

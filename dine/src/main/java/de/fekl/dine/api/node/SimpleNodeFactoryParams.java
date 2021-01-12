package de.fekl.dine.api.node;

public record SimpleNodeFactoryParams<N extends INode> (String id) implements INodeFactoryParams<N> {

}

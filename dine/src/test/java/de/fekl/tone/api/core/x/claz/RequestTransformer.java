package de.fekl.tone.api.core.x.claz;

import de.fekl.dine.api.node.INode;
import de.fekl.dine.api.node.SimpleNode;

public class RequestTransformer extends SimpleNode implements INode, ITransformer{

	public RequestTransformer(String id) {
		super(id);
	}

	public Message transform(Message s) {
		s.setValue(s.getValue() + "x");
		return s;
	}

}

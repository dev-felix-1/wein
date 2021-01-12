package de.fekl.tone.api.core.x.claz;

import de.fekl.dine.api.node.INode;
import de.fekl.dine.api.node.SimpleNode;

public class ResponseTransformer extends SimpleNode implements INode, ITransformer {

	public ResponseTransformer(String id) {
		super(id);
	}

	public Message transform(Message s) {
		s.setValue(s.getValue() + "y");
		return s;
	}

}

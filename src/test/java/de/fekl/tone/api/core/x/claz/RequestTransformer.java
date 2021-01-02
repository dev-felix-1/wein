package de.fekl.tone.api.core.x.claz;

import de.fekl.dine.api.core.INode;
import de.fekl.dine.api.core.SimpleNode;

public class RequestTransformer extends SimpleNode implements INode, ITransformer{

	public Message transform(Message s) {
		s.setValue(s.getValue() + "x");
		return s;
	}

}

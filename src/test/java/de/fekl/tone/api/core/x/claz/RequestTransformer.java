package de.fekl.tone.api.core.x.claz;

import de.fekl.dine.api.core.INodeDeprecated;
import de.fekl.dine.api.core.SimpleNodeDeprecated;

public class RequestTransformer extends SimpleNodeDeprecated implements INodeDeprecated, ITransformer{

	public Message transform(Message s) {
		s.setValue(s.getValue() + "x");
		return s;
	}

}

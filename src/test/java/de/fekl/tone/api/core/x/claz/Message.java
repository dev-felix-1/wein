package de.fekl.tone.api.core.x.claz;

import de.fekl.cone.api.core.IToken;
import de.fekl.cone.api.core.SimpleToken;

public class Message extends SimpleToken implements IToken {

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "msg: "+value;
	}

}

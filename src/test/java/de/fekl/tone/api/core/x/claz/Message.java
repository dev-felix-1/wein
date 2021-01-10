package de.fekl.tone.api.core.x.claz;

import de.fekl.dine.api.state.IToken;
import de.fekl.dine.api.state.SimpleToken;

public class Message extends SimpleToken implements IToken {

	public Message(String id) {
		super(id);
	}

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

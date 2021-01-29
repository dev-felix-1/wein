package de.fekl.stat.test.integration.bpmn;

import java.util.HashMap;
import java.util.Map;

import de.fekl.stat.core.impl.token.SimpleToken;

public class BpmnToken extends SimpleToken implements IBpmnToken {

	private final Map<String, Object> args = new HashMap<>();

	public BpmnToken(String id) {
		super(id);
	}

	@Override
	public Object get(String name) {
		return args.get(name);
	}

	@Override
	public void set(String name, Object value) {
		args.put(name, value);
	}

	@Override
	public Map<String, Object> getAll() {
		return args;
	}

}

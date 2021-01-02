package de.fekl.wein.api.core.builder;

import java.util.ArrayList;
import java.util.List;

public class OutgoingEdgesBuilder {
	private String source;
	private List<String> targets = new ArrayList<>();

	public OutgoingEdgesBuilder target(String target) {
		targets.add(target);
		return this;
	}

	public OutgoingEdgesBuilder source(String source) {
		this.source = source;
		return this;
	}

	public List<String> getTargets() {
		return targets;
	}

	public String getSource() {
		return source;
	}

}

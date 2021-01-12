package de.fekl.wein.api.core.builder;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class EdgesBuilder {
	
	private List<OutgoingEdgesBuilder> outgoing = new ArrayList<>();

	public List<OutgoingEdgesBuilder> getOutgoing() {
		return outgoing;
	}
	
	public EdgesBuilder outgoing (OutgoingEdgesBuilder outgoing) {
		this.outgoing.add(outgoing);
		return this;
	}

	public void setOutgoing(List<OutgoingEdgesBuilder> outgoing) {
		this.outgoing = outgoing;
	}
	
	

}

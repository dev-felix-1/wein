package de.fekl.sepe;

public class EndNodeReachedEvent implements IEndNodeReachedEvent {

	private final String endNode;
	private final String token;

	public EndNodeReachedEvent(String endNode, String token) {
		super();
		this.endNode = endNode;
		this.token = token;
	}

	public String getNodeId() {
		return endNode;
	}

	public String getTokenId() {
		return token;
	}

}

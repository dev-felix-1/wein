package de.fekl.stat.core.api.events;

public interface IEndNodeReachedEvent extends IEvent {

	String getNodeId();

	String getTokenId();

}

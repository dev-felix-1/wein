package de.fekl.sepe;

import de.fekl.esta.api.core.IEvent;

public interface IEndNodeReachedEvent extends IEvent {

	String getNodeId();

	String getTokenId();

}

package de.fekl.wein.api.core;

public interface IContentType<T> {
	
	Class<T> getType();
	
	IFormat getFormat();

}

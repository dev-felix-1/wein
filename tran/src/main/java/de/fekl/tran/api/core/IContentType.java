package de.fekl.tran.api.core;

public interface IContentType<T> {
	
	Class<T> getType();
	
	IFormat getFormat();

}

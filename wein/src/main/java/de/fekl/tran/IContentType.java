package de.fekl.tran;

public interface IContentType<T> {
	
	Class<T> getType();
	
	IFormat getFormat();

}

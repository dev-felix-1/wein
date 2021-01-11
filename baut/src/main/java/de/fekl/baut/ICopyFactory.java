package de.fekl.baut;

public interface ICopyFactory<T> {

	T copy(T object);
	
	T copy();

}

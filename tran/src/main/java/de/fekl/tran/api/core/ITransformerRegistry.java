package de.fekl.tran.api.core;

public interface ITransformerRegistry {
	
	<S,T> void register(ITransformer<S,T> transformer);
	
	void unRegister(String id);
	
	<S,T> ITransformer<S,T> getTransformer(String id);
	
	boolean contains(String id);

}

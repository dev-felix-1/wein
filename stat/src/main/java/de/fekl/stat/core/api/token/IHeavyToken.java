package de.fekl.stat.core.api.token;

public interface IHeavyToken extends IToken {
	
	Object get(String name);
	
	void set(String name, Object value);

}

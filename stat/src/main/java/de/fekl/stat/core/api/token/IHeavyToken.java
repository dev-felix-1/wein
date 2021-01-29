package de.fekl.stat.core.api.token;

import java.util.Map;

public interface IHeavyToken extends IToken {
	
	Map<String,Object> getAll();
	
	Object get(String name);
	
	void set(String name, Object value);

}

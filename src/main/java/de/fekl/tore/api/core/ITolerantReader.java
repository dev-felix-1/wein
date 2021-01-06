package de.fekl.tore.api.core;

public interface ITolerantReader {

	ITolerantReader getProperty(String propertyName);

	void configure(ITolerance tolerance, boolean enabled);

	void configure(ITolerance tolerance, int priority);

	public String getStringValue();

	public Integer getIntValue();

	public Long getLongValue();

	public Boolean getBooleanValue();

	public Object getValue();

}

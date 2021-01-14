package de.fekl.dine.api.base;

public interface IRegistry<I, N extends IIdHolder<I>> {

	void register(N node);

	void unRegister(I id);

	N get(I id);

	boolean contains(I id);

}

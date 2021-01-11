package de.fekl.wein.api.core;

public interface IIntegrationRouteIdentifier<S, T> {

	IContentType<S> getInputType();

	IContentType<T> getOutputType();

	IWsOperationIdentifier getOperation();

}

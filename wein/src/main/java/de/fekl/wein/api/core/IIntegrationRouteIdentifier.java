package de.fekl.wein.api.core;

import de.fekl.tran.api.core.IContentType;

public interface IIntegrationRouteIdentifier<S, T> {

	IContentType<S> getInputType();

	IContentType<T> getOutputType();

	IWsOperationIdentifier getOperation();

}

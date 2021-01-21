package de.fekl.wein.api.core;

import java.util.Objects;

import de.fekl.dine.util.Precondition;
import de.fekl.tran.api.core.IContentType;

public class SimpleIntegrationRouteIdentifier<S, T> implements IIntegrationRouteIdentifier<S, T> {

	private final IWsOperationIdentifier operation;
	private final IContentType<S> inputType;
	private final IContentType<T> outputType;

	public SimpleIntegrationRouteIdentifier(IWsOperationIdentifier operation, IContentType<S> inputType,
			IContentType<T> outputType) {
		super();
		Precondition.isNotNull(operation);
		Precondition.isNotNull(inputType);
		Precondition.isNotNull(outputType);
		this.operation = operation;
		this.inputType = inputType;
		this.outputType = outputType;
	}

	@Override
	public IContentType<S> getInputType() {
		return inputType;
	}

	@Override
	public IContentType<T> getOutputType() {
		return outputType;
	}

	@Override
	public IWsOperationIdentifier getOperation() {
		return operation;
	}

	@Override
	public int hashCode() {
		return Objects.hash(IIntegrationRouteIdentifier.class, operation, inputType, outputType);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IIntegrationRouteIdentifier<?, ?>other) {
			return operation.equals(other.getOperation()) && inputType.equals(other.getInputType())
					&& outputType.equals(other.getOutputType());
		}
		return false;
	}

}

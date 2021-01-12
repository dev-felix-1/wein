package de.fekl.wein.api.core.builder;

import de.fekl.tran.IContentType;
import de.fekl.tran.ITransformation;
import de.fekl.tran.ITransformer;
import de.fekl.tran.SimpleTransformer;
import de.fekl.tran.TransformerNames;
import de.fekl.wein.api.core.IWsOperationIdentifier;

public class TransformerBuilder<S, T> {

	private IContentType<S> sourceContentType;
	private IContentType<T> targetContentType;

	private IWsOperationIdentifier operation;
	private WsOperationIdentifierBuilder operationBuilder;
	private ITransformation<S, T> transformation;

	public IContentType<S> getSourceContentType() {
		return sourceContentType;
	}

	public TransformerBuilder<S, T> sourceContentType(IContentType<S> sourceContentType) {
		this.sourceContentType = sourceContentType;
		return this;
	}

	public IContentType<T> getTargetContentType() {
		return targetContentType;
	}

	public TransformerBuilder<S, T> targetContentType(IContentType<T> targetContentType) {
		this.targetContentType = targetContentType;
		return this;
	}

	public IWsOperationIdentifier getOperation() {
		return operation;
	}

	public TransformerBuilder<S, T> operation(IWsOperationIdentifier operation) {
		this.operation = operation;
		return this;
	}

	public ITransformation<S, T> getTransformation() {
		return transformation;
	}

	public TransformerBuilder<S, T> transformation(ITransformation<S, T> transformation) {
		this.transformation = transformation;
		return this;
	}

	public ITransformer<S, T> buildTransformer() {
		return new SimpleTransformer<>(sourceContentType, targetContentType, transformation,TransformerNames.generateTransformerName());

	}

}

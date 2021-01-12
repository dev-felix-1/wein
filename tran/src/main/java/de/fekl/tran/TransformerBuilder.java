package de.fekl.tran;

public class TransformerBuilder<S, T> {

	private IContentType<S> sourceContentType;
	private IContentType<T> targetContentType;
	private ITransformation<S, T> transformation;
	private String id;
	private ITransformerFactory transformerFactory = new SimpleTransformerFactory();

	public TransformerBuilder<S, T> source(IContentType<S> sourceContentType) {
		this.sourceContentType = sourceContentType;
		return this;
	}

	public TransformerBuilder<S, T> target(IContentType<T> targetContentType) {
		this.targetContentType = targetContentType;
		return this;
	}

	public TransformerBuilder<S, T> transformation(ITransformation<S, T> transformation) {
		this.transformation = transformation;
		return this;
	}

	public TransformerBuilder<S, T> name(String id) {
		this.id = id;
		return this;
	}

	public ITransformer<S, T> build() {
		if (id == null || id.isBlank()) {
			id = TransformerNames.generateTransformerName();
		}
		return transformerFactory.createTransformer(sourceContentType, targetContentType, transformation,id);
	}
}

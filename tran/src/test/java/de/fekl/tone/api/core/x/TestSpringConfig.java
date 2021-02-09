package de.fekl.tone.api.core.x;

import org.springframework.context.annotation.Bean;

import de.fekl.tran.api.core.ITransformation;
import de.fekl.tran.api.core.ITransformer;
import de.fekl.tran.impl.SimpleTransformer;
import de.fekl.tran.impl.StandardContentTypes;

public class TestSpringConfig {

	public static final String IDENTITY_TRANSFORMATION = "IDENTITY_TRANSFORM";
	public static final String IDENTITY_TRANSFORMER = "identityTransformer";
	public static final String IDENTITY_TRANSFORMER_2 = "IDENTITY_TRANSFORMER_2";

	@Bean(name = IDENTITY_TRANSFORMER)
	public ITransformer<Object, Object> getTransformer1(ITransformation<Object, Object> transformation) {
		return new SimpleTransformer<>(StandardContentTypes.OBJECT, StandardContentTypes.OBJECT, transformation,
				IDENTITY_TRANSFORMER, false);
	}
	
	@Bean(name = IDENTITY_TRANSFORMER_2)
	public ITransformer<Object, Object> getTransformer2(ITransformation<Object, Object> transformation) {
		return new SimpleTransformer<>(StandardContentTypes.OBJECT, StandardContentTypes.OBJECT, transformation,
				IDENTITY_TRANSFORMER_2, false);
	}

	@Bean(name = IDENTITY_TRANSFORMATION)
	public ITransformation<Object, Object> getTransformation() {
		return (o) -> (o);
	}

}

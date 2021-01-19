package de.fekl.tone.api.core.x;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.baut.Precondition;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.IConversion;
import de.fekl.tran.api.core.IConverter;
import de.fekl.tran.api.core.IMessage;
import de.fekl.tran.api.core.ITransformation;
import de.fekl.tran.impl.SimpleMessageFactory;
import de.fekl.tran.impl.SimpleTransformer;
import de.fekl.tran.impl.StandardContentTypes;

public class ConverterTest {

	private static class TestConverter extends SimpleTransformer<Integer, String>
			implements IConverter<Integer, String, TestConverterInv, TestConverter> {

		public TestConverter(IContentType<Integer> sourceContentType, IContentType<String> targetContentType,
				ITransformation<Integer, String> transformation, String id) {
			super(sourceContentType, targetContentType, transformation, id, false);
			Precondition.hasClass(transformation, IConversion.class);
		}

		@Override
		public TestConverterInv invert() {
			return new TestConverterInv(getTargetContentType(), getSourceContentType(),
					((IConversion<Integer, String>) getTransformation()).invert(), getId());
		}

	}

	private static class TestConverterInv extends SimpleTransformer<String, Integer>
			implements IConverter<String, Integer, TestConverter, TestConverterInv> {

		public TestConverterInv(IContentType<String> sourceContentType, IContentType<Integer> targetContentType,
				ITransformation<String, Integer> transformation, String id) {
			super(sourceContentType, targetContentType, transformation, id, false);
			Precondition.hasClass(transformation, IConversion.class);
		}

		@Override
		public TestConverter invert() {
			return new TestConverter(getTargetContentType(), getSourceContentType(),
					((IConversion<String, Integer>) getTransformation()).invert(), getId());
		}

	}

	private static enum IntToStringConversion implements IConversion<Integer, String> {

		INSTANCE;

		@Override
		public String transform(Integer source) {
			if (source == null) {
				return null;
			}
			return String.valueOf(source);
		}

		@Override
		public ITransformation<String, Integer> invert() {
			return StringToIntConversion.INSTANCE;
		}

	}

	private static enum StringToIntConversion implements IConversion<String, Integer> {

		INSTANCE;

		@Override
		public Integer transform(String source) {
			if (source == null) {
				return null;
			}
			return Integer.parseInt(source);
		}

		@Override
		public ITransformation<Integer, String> invert() {
			return IntToStringConversion.INSTANCE;
		}

	}

	@Test
	public void test() {
		var converter = new TestConverter(StandardContentTypes.INTEGER, StandardContentTypes.INTEGER_STRING,
				IntToStringConversion.INSTANCE, "intToStringConverter");

		IMessage<Integer> initialMessage = new SimpleMessageFactory().createMessage(13);

		IMessage<String> transformed = converter.transform(initialMessage);
		IMessage<Integer> reTransformed = converter.invert().transform(transformed);

		Assertions.assertEquals(initialMessage.getValue(), reTransformed.getValue());
	}

}

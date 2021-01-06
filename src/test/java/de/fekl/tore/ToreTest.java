package de.fekl.tore;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.tore.api.core.ITolerantReader;
import de.fekl.tore.api.core.ITolerantReaderFactory;
import de.fekl.tore.api.core.Tolerance;
import de.fekl.tore.api.core.TolerantReaderContext;
import de.fekl.tore.api.core.TolerantReaderRegistry;
import de.fekl.tore.api.core.reader.TolerantMapReader;
import de.fekl.tore.api.core.reader.TolerantMapReaderFactory;
import de.fekl.tore.api.core.reader.TolerantObjectReader;

public class ToreTest {

	static interface A {

	}

	static interface B extends A {

	}

	static interface C {

	}

	public static class ObjectToRead {
		private final String valueToRead;

		ObjectToRead(String valueToRead) {
			this.valueToRead = valueToRead;
		}

		public String getValueToRead() {
			return valueToRead;
		}
	}

	@Test
	public void testRegistry() {

		TolerantReaderRegistry tolerantReaderRegistry = new TolerantReaderRegistry();
		tolerantReaderRegistry.getTolerantReaderFactory(Object.class);

		tolerantReaderRegistry.registerTolerantReaderFactory(Object.class, new ITolerantReaderFactory<Object>() {
			@Override
			public ITolerantReader createTolerantReader(Object objectToRead) {
				return null;
			}

			@Override
			public void setContext(TolerantReaderContext context) {
				// TODO Auto-generated method stub

			}
		});
		ITolerantReaderFactory<Object> tolerantReaderFactory = tolerantReaderRegistry
				.getTolerantReaderFactory(Object.class);
		Assertions.assertNotNull(tolerantReaderFactory);

		tolerantReaderRegistry.registerTolerantReaderFactory(A.class, new ITolerantReaderFactory<A>() {
			@Override
			public ITolerantReader createTolerantReader(A objectToRead) {
				return null;
			}

			@Override
			public void setContext(TolerantReaderContext context) {
				// TODO Auto-generated method stub

			}
		});
		tolerantReaderFactory = tolerantReaderRegistry.getTolerantReaderFactory(Object.class);
		Assertions.assertNotNull(tolerantReaderFactory);
		ITolerantReaderFactory<A> tolerantReaderFactoryForA = tolerantReaderRegistry.getTolerantReaderFactory(A.class);
		Assertions.assertNotNull(tolerantReaderFactoryForA);
		ITolerantReaderFactory<B> tolerantReaderFactoryForB = tolerantReaderRegistry.getTolerantReaderFactory(B.class);
		Assertions.assertNotNull(tolerantReaderFactoryForB);
		Assertions.assertEquals(tolerantReaderFactoryForA, tolerantReaderFactoryForB);
		ITolerantReaderFactory<C> tolerantReaderFactoryForC = tolerantReaderRegistry.getTolerantReaderFactory(C.class);
		Assertions.assertNotNull(tolerantReaderFactoryForC);
		Assertions.assertEquals(tolerantReaderFactory, tolerantReaderFactoryForC);

	}

	@Test
	public void testObjectReader() {
		TolerantObjectReader tolerantObjectReader = new TolerantObjectReader(new Object());
		tolerantObjectReader.getStringValue();

		tolerantObjectReader = new TolerantObjectReader(new ObjectToRead("hello"));
		ITolerantReader property = tolerantObjectReader.getProperty("valueToRead");
		Assertions.assertEquals("hello", property.getStringValue());

		tolerantObjectReader = new TolerantObjectReader(new ObjectToRead("hello"));
		property = tolerantObjectReader.getProperty("valueTOREAD");
		Assertions.assertEquals(null, property.getStringValue());

		tolerantObjectReader = new TolerantObjectReader(new ObjectToRead("hello"));
		tolerantObjectReader.configure(Tolerance.CASE, true);
		property = tolerantObjectReader.getProperty("valueTOREAD");
		Assertions.assertEquals("hello", property.getStringValue());

	}

	@Test
	public void testMapReader() {
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put("valueToRead", "hello");
				put("nestedMap", new HashMap<String, Object>() {
					{
						put("valueToRead", "hello");
						put("layer2prop", "hello2");
						put("layer3propDup", "hello3");
						put("nestedMap", new HashMap<String, Object>() {
							{
								put("layer3propDUP", "hello3");
								put("hello4", "hello4");
							}
						});
					}
				});
			}
		};
		TolerantMapReader tolerantObjectReader = new TolerantMapReader(map);

		tolerantObjectReader.configure(Tolerance.CASE, true);
		Assertions.assertEquals("hello", tolerantObjectReader.getProperty("valueToRead").getStringValue());
		Assertions.assertEquals("hello", tolerantObjectReader.getProperty("valUEToReaD").getStringValue());
		Assertions.assertNull(tolerantObjectReader.getProperty("notAProperty").getStringValue());

		tolerantObjectReader.configure(Tolerance.POSITION, true);
		Assertions.assertEquals("hello2", tolerantObjectReader.getProperty("layer2prop").getStringValue());
		Assertions.assertNull(tolerantObjectReader.getProperty("layer3propDUP").getStringValue());

		tolerantObjectReader.configure(Tolerance.CASE, false);
		Assertions.assertEquals("hello2", tolerantObjectReader.getProperty("layer2prop").getStringValue());
		Assertions.assertEquals("hello3", tolerantObjectReader.getProperty("layer3propDup").getStringValue());

		TolerantReaderContext.DEFAULT.registerTolerantReaderFactory(Map.class, new TolerantMapReaderFactory());
		ITolerantReader defaultTolerantReader = TolerantReaderContext.DEFAULT.createTolerantReader(map);

		Assertions.assertEquals("hello4", defaultTolerantReader.getProperty("nestedMap").getProperty("nestedMap")
				.getProperty("hello4").getStringValue());

	}

}

package de.fekl.tore.api.core.reader;

import de.fekl.tore.api.core.ITolerantReader;
import de.fekl.tore.api.core.ITolerantReaderFactory;
import de.fekl.tore.api.core.TolerantReaderContext;

public class TolerantObjectReaderFactory implements ITolerantReaderFactory<Object> {

	private TolerantReaderContext context;

	public void setContext(TolerantReaderContext context) {
		this.context = context;
	}

	@Override
	public ITolerantReader createTolerantReader(Object objectToRead) {
		return new TolerantObjectReader(objectToRead, context);
	}

}

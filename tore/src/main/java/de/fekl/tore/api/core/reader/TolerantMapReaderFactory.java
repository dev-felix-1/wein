package de.fekl.tore.api.core.reader;

import java.util.Map;

import de.fekl.tore.api.core.ITolerantReader;
import de.fekl.tore.api.core.ITolerantReaderFactory;
import de.fekl.tore.api.core.TolerantReaderContext;

@SuppressWarnings("rawtypes")
public class TolerantMapReaderFactory implements ITolerantReaderFactory<Map> {

	private TolerantReaderContext context;

	public void setContext(TolerantReaderContext context) {
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ITolerantReader createTolerantReader(Map objectToRead) {
		return new TolerantMapReader(objectToRead, context);
	}

}

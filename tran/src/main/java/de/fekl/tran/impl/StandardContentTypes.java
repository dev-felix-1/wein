package de.fekl.tran.impl;

import de.fekl.baut.Precondition;
import de.fekl.tran.api.core.IContentType;
import de.fekl.tran.api.core.IFormat;
import de.fekl.tran.impl.StandardFormats.XML;

public class StandardContentTypes {

	private StandardContentTypes() {

	}

	public static final IContentType<String> PRETTY_XML_STRING = new IContentType<String>() {

		@Override
		public Class<String> getType() {
			return String.class;
		}

		@Override
		public IFormat getFormat() {
			return StandardFormats.XML.PRETTY_STRING;
		}

		@Override
		public String toString() {
			return "PRETTY_XML_STRING";
		}
	};

	@SuppressWarnings("unchecked")
	public static <T> IContentType<T> byName(String name) {
		Precondition.isNotEmpty(name);
		switch (name) {
		case "PRETTY_XML_STRING":
		case "XML_STRING":
		case "PRETTY_XML":
		case "XML":
		case "xml":
			return (IContentType<T>) PRETTY_XML_STRING;
		default:
			throw new IllegalArgumentException(name);
		}
	}

}

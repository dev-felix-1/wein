package de.fekl.tran.impl;

import de.fekl.baut.Precondition;
import de.fekl.tran.api.core.IContentType;

public class StandardContentTypes {

	private StandardContentTypes() {

	}

	public static final IContentType<String> STRING = new SimpleContentType<>(String.class, StandardFormats.NONE) {

		@Override
		public String toString() {
			return "STRING";
		}
	};

	public static final IContentType<String> PRETTY_XML_STRING = new SimpleContentType<>(String.class,
			StandardFormats.XML.PRETTY_STRING) {

		@Override
		public String toString() {
			return "PRETTY_XML_STRING";
		}
	};

	public static final IContentType<Object> OBJECT = new SimpleContentType<>(Object.class, StandardFormats.NONE) {

		@Override
		public String toString() {
			return "UNKNOWN_OBJECT";
		}
	};

	public static final IContentType<Integer> INTEGER = new SimpleContentType<>(Integer.class, StandardFormats.NONE) {

		@Override
		public String toString() {
			return "INTEGER";
		}
	};

	public static final IContentType<String> INTEGER_STRING = new SimpleContentType<>(String.class,
			StandardFormats.NONE) {

		@Override
		public String toString() {
			return "INTEGER_STRING";
		}
	};

	@SuppressWarnings("unchecked")
	public static <T> IContentType<T> byName(String name) {
		Precondition.isNotEmpty(name);
		switch (name) {
		case "STRING":
		case "string":
		case "STR":
		case "str":
			return (IContentType<T>) STRING;
		case "PRETTY_XML_STRING":
		case "XML_STRING":
		case "PRETTY_XML":
		case "XML":
		case "xml":
			return (IContentType<T>) PRETTY_XML_STRING;
		case "UNKNOWN_OBJECT":
		case "OBJECT":
		case "object":
		case "obj":
		case "undefined":
		case "unknown":
		case "n/a":
		case "none":
			return (IContentType<T>) OBJECT;
		case "INTEGER":
		case "integer":
		case "INT":
		case "int":
			return (IContentType<T>) INTEGER;
		case "INTEGER_STRING":
		case "integer-string":
		case "INT_STR":
		case "int-str":
			return (IContentType<T>) INTEGER_STRING;
		default:
			throw new IllegalArgumentException(name);
		}
	}

}

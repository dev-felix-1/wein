package de.fekl.wein.groovy.support;

import java.util.List;

import org.junit.jupiter.api.Test;
import java.lang.annotation.*;

import de.fekl.dine.api.core.INet;
import de.fekl.wein.api.core.ITransformer;
import de.fekl.wein.api.core.IWsEndpointIdentifier;
import de.fekl.wein.api.core.IWsOperationIdentifier;
import de.fekl.wein.api.core.SimpleMessage;
import de.fekl.wein.api.core.builder.NetBuilder;
//import de.fekl.wein.groovy.support.TestEndpointBuilder;
//import de.fekl.wein.groovy.support.TestOperationBuilder;
//import de.fekl.wein.groovy.support.TestRouteBuilder;
//import de.fekl.wein.groovy.support.TestTransformerBuilder;

public class TestGroovySupport {

	@Test
	public void test() {

//		INet test = new TestRouteBuilder().testListBuilder();
//		System.err.println(test.print());

		INet test2 = new TestRouteBuilder().testClosureBuilder().buildNet();
		System.err.println(test2.print());
		INet test3 = new TestRouteBuilder().testShortcutClosureBuilder().buildNet();
		System.err.println(test3.print());
		INet test4 = new TestRouteBuilder().testSuperShortcutClosureBuilder().buildNet();
		System.err.println(test4.print());
		INet test5 = new TestRouteBuilder().testInlineNodeBuilder().withAlphabeticalNames().buildNet();
		System.err.println(test5.print());
		List<NetBuilder> test6 = new TestRouteBuilder().testIntegrationRouteBuilder();
		test6.stream().forEach({ b ->
			INet buildNet = b.withAlphabeticalNames().buildNet();
			System.err.println(buildNet.print());
		});
		INet test7 = new TestRouteBuilder().testCodeInjectedBuilder().buildNet();
		System.err.println(test7.print());

	}
	
	@Test
	public void testEndpointBuilder() {
		
//		INet test = new TestRouteBuilder().testListBuilder();
//		System.err.println(test.print());
		
		IWsEndpointIdentifier test2 = new TestEndpointBuilder().testClosureBuilder().buildEndpoint();
		System.err.println(test2);

		
	}
	
	
	@Test
	public void testOperationBuilder() {
		IWsOperationIdentifier test2 = new TestOperationBuilder().testClosureBuilder().buildOperation();
		System.err.println(test2);
	}
	
	@Test
	public void testTransformerBuilder() {
		ITransformer test2 = new TestTransformerBuilder().testClosureBuilder().buildTransformer();
		System.err.println(test2.transform(new SimpleMessage("hello")));
		System.err.println(test2);
		ITransformer test3 = new TestTransformerBuilder().testClosureBuilderShort().buildTransformer();
		System.err.println(test3.transform(new SimpleMessage("hello")));
		System.err.println(test3);
	}

}

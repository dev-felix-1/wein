package de.fekl.wein.groovy.support;

import org.junit.jupiter.api.Test;

import de.fekl.dine.api.tree.ISpongeNet
import de.fekl.wein.api.core.IWsEndpointIdentifier;
import de.fekl.wein.api.core.IWsOperationIdentifier;
import groovy.transform.CompileStatic
//import de.fekl.wein.groovy.support.TestEndpointBuilder;
//import de.fekl.wein.groovy.support.TestOperationBuilder;
//import de.fekl.wein.groovy.support.TestRouteBuilder;
//import de.fekl.wein.groovy.support.TestTransformerBuilder;

@CompileStatic
public class TestGroovySupport {
	
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
	

}

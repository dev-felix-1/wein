package de.fekl.tone.api.core.x;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.*;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import de.fekl.tran.api.core.ITransformer;

@SpringJUnitConfig(TestSpringConfig.class)
public class TranSpringTEst {

	@Autowired
	ApplicationContext context;
	
	@Autowired
	ITransformer<Object, Object> identityTransformer;
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(options().usingFilesUnderClasspath("files"));

	@Test
	public void test() {
		System.err.println(identityTransformer.getId());
		System.out.println(String.format("%s", context.getBeansOfType(ITransformer.class)));

		stubFor(get(urlPathMatching("/baeldung/.*"))
				  .willReturn(aResponse()
				  .withStatus(200)
				  .withHeader("Content-Type", "application/json")
				  .withBody("\"testing-library\": \"WireMock\"")));
		
		//define transformer with input object, output wilcard remote rest client, 
		//define transformer with input wildcard remote rest client, output object
		//use tranClient like
		/*
		 * var route = Tran.discoverChain()
		 *  	.wildcardProperty("operation", "doSometin")
		 *  	.first()
		 *  
		 * var processor = new Processor()
		 * 
		 * processor.process("hello", route)
		 * ||
		 * 
		 * var result = Tran.discoverChain()
		 * 	.wildcardProperty("operation", "doSometin")
		 * 	.process("hello")
		 * 
		 * ||
		 * 
		 *  
		 *  --------
		 *  
		 *  ToRequestTransformer {
		 *  	in < {
		 *  		Object
		 *  	}
		 *  	out < {
		 *  		Wildcard : Object, WS-Request
		 *  	}
		 *  
		 *  	transformation < { x ->
		 *  		y
		 *  	}	
		 *  }
		 *  
		 *  --------
		 *  
		 *  FromResponseTransformer {
		 *  	in < { Wildcard: Object, WS-Response }
		 *  	out < Object
		 *  }
		 * 
		 * 
		 */
		
		 
	}
}

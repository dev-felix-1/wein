package de.fekl.wein.groovy.support

import org.junit.jupiter.api.Test

class TestIpService {
	
	@Test 
	public void testRemoteService() {
		def get = new URL("https://ipinfo.io/8.8.8.8").openConnection();
		def getRC = get.getResponseCode();
		println(getRC);
		if(getRC.equals(200)) {
			println(get.getInputStream().getText());
		}
	}
	
	@Test 
	public void testRemoteService2() {
		def transformer =  new TransformerBuilder().transformer {
			name ('test')
			operation ('doShit')
			input (StandardContentTypes.PRETTY_XML_STRING)
			output (StandardContentTypes.PRETTY_XML_STRING)
			transform { input ->
				input + 'x'
			}
		}
		def get = new URL("https://ipinfo.io/8.8.8.8").openConnection();
		def getRC = get.getResponseCode();
		println(getRC);
		if(getRC.equals(200)) {
			println(get.getInputStream().getText());
		}
	}
}

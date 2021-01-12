package de.fekl.wein.groovy.support

import de.fekl.tran.ITransformation
import de.fekl.tran.StandardContentTypes

class TestTransformerBuilder {

	public de.fekl.wein.api.core.builder.TransformerBuilder testClosureBuilder() {

		return new TransformerBuilder().transformer {
			name ('test')
			operation ('doShit')
			input (StandardContentTypes.PRETTY_XML_STRING)
			output (StandardContentTypes.PRETTY_XML_STRING)
			transform { input ->
				input + 'x'
			}
		}
	}
	
	public de.fekl.wein.api.core.builder.TransformerBuilder testClosureBuilderShort() {
		
		return new TransformerBuilder(). transformer ('trans2') {
			operation 'doShit'
			input StandardContentTypes.PRETTY_XML_STRING
			output StandardContentTypes.PRETTY_XML_STRING
			transform { input ->
			input + 'x'
			}
		} 
	}
}

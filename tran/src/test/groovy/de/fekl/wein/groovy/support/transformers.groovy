package de.fekl.wein.groovy.support

transformer {
	inputContentType 'string'
	outputContentType 'string'
	transformation { o -> o + 'A' }
}

transformer {
	inputContentType 'string'
	outputContentType 'string'
	transformation { o -> o + 'B' }
}
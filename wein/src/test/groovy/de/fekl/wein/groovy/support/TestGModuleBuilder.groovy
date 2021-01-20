package de.fekl.wein.groovy.support

import org.junit.jupiter.api.Test

import de.fekl.tran.impl.StandardContentTypes

class TestGModuleBuilder {

	@Test
	public void test1() {

		def b = new GModuleBuilder().module {
			endpoint {
				name 'endpointX'
				version '0'
			}
			operations {
				operation {
					name 'opX'
					endpoint {
						name 'endpointX'
						version '0'
					}
				}
			}
			
			transformers {
				transformer {
					id 'A'
					inputContentType StandardContentTypes.STRING
					outputContentType StandardContentTypes.STRING
					transformation { o -> o + 'D' }
				}
				transformer {
					id 'B'
					inputContentType StandardContentTypes.STRING
					outputContentType StandardContentTypes.STRING
					transformation { o -> o + 'D' }
				}
				transformer {
					id 'C'
					inputContentType StandardContentTypes.STRING
					outputContentType StandardContentTypes.STRING
					transformation { o -> o + 'D' }
				}
				transformer {
					id 'D'
					inputContentType StandardContentTypes.STRING
					outputContentType StandardContentTypes.STRING
					transformation { o -> o + 'D' }
				}
			}
			
			routes {
				route {
					id 'routeX'
					edges {
						edge (source: 'A', target: ['C', 'B'])
						edge (source: 'B', target: 'D')
						edge (source: 'C', target: 'D')
					}
				}
			}
			
			mappings {
				mapping {
					operation 'opX'
					route 'routeX'
				}
			}
		}
		
		def module = b.build()
		println module

	}
	
	@Test
	public void test2() {
		
		def b = new GModuleBuilder().module {
			
			operations {
				endpoint (name : 'endpointX', version : '0')
				
				operation {
					name 'opX'
				}
				operation {
					name 'opY'
				}
			}
			
			routes {
				route {
					id 'routeX'
					
					nodes {
						node {
							id 'A'
							inputContentType StandardContentTypes.STRING
							outputContentType StandardContentTypes.STRING
							transformation { o -> o + 'D' }
						}
						node {
							id 'B'
							inputContentType StandardContentTypes.STRING
							outputContentType StandardContentTypes.STRING
							transformation { o -> o + 'D' }
						}
						node {
							id 'C'
							inputContentType StandardContentTypes.STRING
							outputContentType StandardContentTypes.STRING
							transformation { o -> o + 'D' }
						}
						node {
							id 'D'
							inputContentType StandardContentTypes.STRING
							outputContentType StandardContentTypes.STRING
							transformation { o -> o + 'D' }
						}
					}
					
					edges {
						edge (source: 'A', target: ['C', 'B'])
						edge (source: 'B', target: 'D')
						edge (source: 'C', target: 'D')
					}
				}
			}
			
			mappings {
				mapping {
					operation 'opX'
					route 'routeX'
				}
			}
		}
		
		def module = b.build()
		println module
				
	}

	
}

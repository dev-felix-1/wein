package de.fekl.wein.groovy.support

import de.fekl.tran.StandardContentTypes
import de.fekl.wein.api.core.builder.NetBuilder

class TestModuleBuilder {

	public NetBuilder testClosureBuilder() {

		return new ModuleBuilder().module {
			name ('test')
			routes {
				route {
					inputType (StandardContentTypes.PRETTY_XML_STRING)
					outputType (StandardContentTypes.PRETTY_XML_STRING)
					operation {
						name ('get-shit')
						endpoint {
							name ('web-api-1')
							version ('0.1')
						}
					}
				}
			}
		}
	}

	public NetBuilder testClosureBuilderWithExtEndpoint() {

		return new ModuleBuilder().module {
			name ('test')
			routes {
				route {
					inputType (StandardContentTypes.PRETTY_XML_STRING)
					outputType (StandardContentTypes.PRETTY_XML_STRING)
					operation {
						name ('get-shit')
						endpoint {
							name ('web-api-1')
							version ('0.1')
						}
					}
				}
			}
		}
	}

	public NetBuilder testClosureBuilderWithExtEndpoint2() {

		return new ModuleBuilder().module {
			name ('test')
			routes {
				route ('route1') {
					inputType (StandardContentTypes.PRETTY_XML_STRING)
					outputType (StandardContentTypes.PRETTY_XML_STRING)
					operation {
						name ('get-shit')
						endpoint {
							name ('web-api-1')
							version ('0.1')
						}
					}
					path {
						transformer (name: 'A', inject: true)
						transformer (name: 'B', inject: true)
					}
				}
				route ('route2')  {
					inputType (StandardContentTypes.PRETTY_XML_STRING)
					outputType (StandardContentTypes.PRETTY_XML_STRING)
					operation {
						name ('get-piss')
						endpoint {
							name ('web-api-1')
							version ('0.1')
						}
					}
					path {
						transformer (name: 'C', inject: true)
						transformer (name: 'D', inject: true)
					}
				}
			}
		}
	}

	public NetBuilder testClosureBuilderWithExtEndpoint3() {

		return new ModuleBuilder().module {
			name ('test')
			routes {
				route {
					operation (operation1)
					path {
						transformer (name: 'A', inject: true)
						transformer (name: 'B', inject: true)
					}
				}
				route {
					operation ('operationName2')
					path (inject: true)
				}
			}
		}
	}

	public NetBuilder testClosureBuilderWithExtEndpoint4() {

		return new ModuleBuilder().module ('newIntegModule') {
			route {
				operation {
					name ('get-shit')
					endpoint {
						name ('web-api-1')
						version ('0.1')
					}
				}
				path (inject: true)
			}
			route {
				operation ('operationName2')
				path (inject: true)
			}
		}
	}
	
	public NetBuilder testClosureBuilderWithExtEndpoint5() {
		
		return new ModuleBuilder().module ('newIntegModule') {			
			routes (inject: true)
		}
	}
}

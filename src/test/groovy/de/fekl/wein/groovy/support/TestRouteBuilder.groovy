package de.fekl.wein.groovy.support

import de.fekl.dine.api.core.INet
import de.fekl.dine.api.core.INodeDeprecated
import de.fekl.wein.api.core.builder.NetBuilder

class TestRouteBuilder {

	//	public INet testListBuilder() {
	//
	//		return new RouteBuilder().route('testRoute') {
	//			nodes (['a', 'b', 'c'])
	//			edges ([
	//				'a' : 'b',
	//				'b' : 'c'
	//			])
	//		}
	//	}

	class NodeImpl implements INodeDeprecated {
		String print() { 'nodeImpl' }
	}
	class NodeImpl2 implements INodeDeprecated {
		String print() { 'nodeImpl2' }
	}

	public NetBuilder testClosureBuilder() {

		return new RouteBuilder().route('testRoute') {
			nodes {
				node ('a') {
					role ('START')
				}
				node ('b') {
					impl new NodeImpl()
				}
				node ('c') {
					role ('END')
				}
				node ('d')
				node ('e')
			}

			edges {
				from ('a') {
					to ('b')
					to ('d')
				}
				from ('b') {
					to ('c')
				}
			}
		}
	}

	public NetBuilder testShortcutClosureBuilder() {

		return new RouteBuilder().route('testRoute') {
			node ('a') {
				role ('START')
			}
			node ('b') {
				impl new NodeImpl()
			}
			node ('c') {
				role ('END')
			}
			node ('d')
			node ('e')

			from ('a') {
				to ('b')
				to ('d')
			}
			from ('b') {
				to ('c')
			}
		}
	}

	public NetBuilder testSuperShortcutClosureBuilder() {

		return new RouteBuilder().route('testRoute') {
			node ('a') {
				role ('START')
				to ('b')
				to ('d')
			}
			node ('b') {
				impl new NodeImpl()
				to ('c')
			}
			node ('c') {
				role ('END')
			}
			node ('d')
			node ('e')
		}
	}

	public NetBuilder testInlineNodeBuilder() {

		return new RouteBuilder().route('testRoute3') {
			node {
				role ('START')

				to {
					impl new NodeImpl()
					to {
						role ('END')
					}
				}

				to {

				}
			}
			node {}
		}
	}

	public List<NetBuilder> testIntegrationRouteBuilder() {

		def converters = [new NodeImpl(), new NodeImpl2()]

		def getConverterImpl = {
			return new NodeImpl2()
		}

		def getTransformer1 = {
			return new NodeImpl2()
		}

		converters.collect { converter ->
			new RouteBuilder().route("integRoute[${converter.print()}]") {
				node {
					role ('START')

					to {
						impl converter

						to {
							impl getTransformer1()

							to {
								role ('END')
							}
						}
					}
				}
			}
		}
	}

	public NetBuilder testCodeInjectedBuilder() {

		return new RouteBuilder().route('testRoute') {
			node ('a') {
				role ('START')
				to ('b')
			}			
			['a','b','c','z'].each { 
				node (it) {
					to('d')
				}
			}
			node ('d') {
				role ('END')
			}
		}
	}
}

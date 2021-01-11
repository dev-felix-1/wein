package de.fekl.wein.groovy.support

import de.fekl.dine.api.core.SimpleNode
import de.fekl.dine.api.graph.INode
import de.fekl.wein.api.core.builder.NetBuilder
import groovy.transform.CompileStatic

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

	class NodeImpl extends SimpleNode implements INode {
		NodeImpl(String s){super(s)}
		String toString() { 'nodeImpl' }
	}
	class NodeImpl2 extends SimpleNode implements INode {
		NodeImpl2(String s){super(s)}
		String toString() { 'nodeImpl2' }
	}

	public NetBuilder testClosureBuilder() {

		return new RouteBuilder().route('testRoute') {
			nodes {
				node ('a') {
					role ('START')
				}
				node ('b') {
					impl new NodeImpl('b')
				}
				node ('c') {
					role ('END')
				}
				node ('d') {
			
				}
				node ('e') {
					
				}
			}

			edges {
				from ('a') {
					to ('b')
					to ('d')
				}
				from ('b') {
					to ('c')
				}
				from ('c') { to ('d') }
				from ('d') { to ('e') }
			}
		}
	}

	public NetBuilder testShortcutClosureBuilder() {

		return new RouteBuilder().route('testRoute') {
			node ('a') {
				role ('START')
			}
			node ('b') {
				impl new NodeImpl('b')
			}
			node ('c') {
				role ('END')
			}

			from ('a') {
				to ('b')
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
				impl new NodeImpl('b')
				to ('c')
			}
			node ('c') {
				role ('END')
			}
			node ('d') {
				
			}
		}
	}

	public NetBuilder testInlineNodeBuilder() {

		return new RouteBuilder().route('testRoute3') {
			node {
				role ('START')

				to {
					impl new NodeImpl('b')
					to {
						role ('END')
					}
				}

				to {

				}
			}
		}
	}

	public List<NetBuilder> testIntegrationRouteBuilder() {

		def converters = [new NodeImpl(), new NodeImpl2()]

		def getConverterImpl = {
			return new NodeImpl2('v')
		}

		def getTransformer1 = {
			return new NodeImpl2('w')
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

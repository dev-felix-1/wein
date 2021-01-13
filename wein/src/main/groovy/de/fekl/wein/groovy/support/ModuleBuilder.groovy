package de.fekl.wein.groovy.support

import groovy.transform.CompileStatic

@CompileStatic
class ModuleBuilder extends BuilderSupport {

	@Override
	protected void setParent(Object parent, Object child) {
		println ("parent: $parent - child: $child")
		// TODO Auto-generated method stub

	}

	@Override
	protected Object createNode(Object name) {
		println current
		switch(name) {
			default: throw new IllegalArgumentException("Invalid keyword $name")
		}
	}


	//	return new RouteBuilder().route('testRoute') {
	//		nodes {
	//			node 'a' {
	//				role 'START'
	//			}
	//			node 'b' {
	//				impl new NodeImpl()
	//			}
	//			node 'c' {
	//				role 'END'
	//			}
	//			node 'd'
	//			node 'e'
	//		}
	//
	//		edges {
	//			from 'a' {
	//				to 'b'
	//				to 'b2'
	//			}
	//			from 'b' {
	//				to 'c'
	//			}
	//		}
	//	}
	@Override
	protected Object createNode(Object name, Object value) {
		println current
		switch(name) {
			default: throw new IllegalArgumentException("Invalid keyword $name")
		}
	}

	@Override
	protected Object createNode(Object name, Map attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object createNode(Object name, Map attributes, Object value) {
		// TODO Auto-generated method stub
		return null;
	}



}

package de.fekl.wein.groovy.support

class GListBuilder<C,P> {
	
	P parent
	List<C> children = []
	
	def add(C child) {
		children += child
	}
	
	def addAll(List<C> children) {
		children += children
	}
	
}

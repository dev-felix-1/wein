package de.fekl.tone.api.core.x;

import org.junit.jupiter.api.Test;

import de.fekl.dine.api.core.NodeRoles;
import de.fekl.wein.api.core.builder.EdgesBuilder;
import de.fekl.wein.api.core.builder.NetBuilder;
import de.fekl.wein.api.core.builder.NodeBuilder;
import de.fekl.wein.api.core.builder.NodesBuilder;
import de.fekl.wein.api.core.builder.OutgoingEdgesBuilder;

public class TestBuilderSupport {

	@Test
	public void test() {
		//@formatter:off
		System.err.println(new NetBuilder()
			.nodes(
				new NodesBuilder()
					.node(new NodeBuilder()
							.id("A")
							.role(NodeRoles.START))
					.node(new NodeBuilder()
							.id("B")
							.role(NodeRoles.END))
			)
			.edges(
					new EdgesBuilder().outgoing(new OutgoingEdgesBuilder().source("A").target("B"))).buildNet());

	}
	
	@Test
	public void testShortcut() {
		//@formatter:off
		System.err.println(new NetBuilder()
						.node(new NodeBuilder()
								.id("A")
								.role(NodeRoles.START))
						.node(new NodeBuilder()
								.id("B")
								.role(NodeRoles.END)
								)
						.from(new OutgoingEdgesBuilder().source("A")
								.target("B")
							 )
						.buildNet());
		
	}
	
	@Test
	public void testEmptyNames() {
		//@formatter:off
		System.err.println(new NetBuilder()
				.node(new NodeBuilder()
						.role(NodeRoles.START)
						.to(new NodeBuilder()
								.role(NodeRoles.END)))
				.buildNet());
		
	}
}

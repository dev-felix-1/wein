package de.fekl.dine.core.api.node;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AbstractNodeBuilderTest {

	@Test
	public <A extends AbstractNodeBuilder<INode, INodeFactory<INode>, A>> void test() {
		var builder = new AbstractNodeBuilder<INode, INodeFactory<INode>, A>() {

			@Override
			protected INode doBuild() {
				return () -> "id";
			}
		};
		INode node = builder.build();
		Assertions.assertNotNull(node);
		Assertions.assertEquals("id", node.getId());
	}

}

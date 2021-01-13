package de.fekl.dine.api.edge;

import java.util.Objects;

import de.fekl.baut.Precondition;

public class SimpleEdge implements IEdge {

	private final String source;
	private final String target;

	public SimpleEdge(String source, String target) {
		super();
		Precondition.isNotEmpty(source);
		Precondition.isNotEmpty(target);
		this.source = source;
		this.target = target;
	}

	@Override
	public String getSource() {
		return source;
	}

	@Override
	public String getTarget() {
		return target;
	}

	@Override
	public String toString() {
		return String.format("{%s -> %s}", source, target);
	}

	@Override
	public int hashCode() {
		return Objects.hash(SimpleEdge.class, source, target);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IEdge) {
			IEdge other = (IEdge) obj;
			return this.getSource().equals(other.getSource()) && this.getTarget().equals(other.getTarget());
		}
		return false;

	}

}

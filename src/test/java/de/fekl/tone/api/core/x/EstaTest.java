package de.fekl.tone.api.core.x;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.fekl.dine.api.state.ITokenStore;
import de.fekl.dine.api.state.SimpleToken;
import de.fekl.dine.api.state.SimpleTokenStore;
import de.fekl.esta.api.core.IStateHasChangedEvent;
import de.fekl.esta.api.core.SimpleStateContainer;

public class EstaTest {

	private static final String NID_A = "A";
	private static final String NID_B = "B";

	@Test
	public void testStateContainerWithNet() {
		ITokenStore tokenState = new SimpleTokenStore();
		System.err.println(ITokenStore.print(tokenState));

		SimpleStateContainer<ITokenStore> simpleStateContainer = new SimpleStateContainer<>(tokenState);
		simpleStateContainer.changeState(current -> {
			ITokenStore newState = new SimpleTokenStore();
			current.getTokenMapping().forEach((k, s) -> {
				s.forEach(v -> newState.putToken(k, v));
			});
			newState.putToken(NID_A, new SimpleToken("token1"));
			return newState;
		});

		IStateHasChangedEvent<ITokenStore> poll = simpleStateContainer.getStateChangedEvents().poll();

		System.err.println(ITokenStore.print(simpleStateContainer.getCurrentState()));
		System.err.println(ITokenStore.print(poll.getSourceState()));
		System.err.println(ITokenStore.print(poll.getTargetState()));

		tokenState.putToken(NID_A, new SimpleToken("t1"));
		tokenState.putToken(NID_A, new SimpleToken("t2"));
		tokenState.putToken(NID_B, new SimpleToken("t3"));
		tokenState.putToken(NID_B, new SimpleToken("t4"));
		tokenState.removeToken(NID_B, "token4");
		System.err.println(ITokenStore.print(tokenState));

	}

	@Test
	public void testStateContainer() {
		SimpleStateContainer<String> simpleStateContainer = new SimpleStateContainer<>("init");
		Assertions.assertEquals(0, simpleStateContainer.getStateChangedEvents().size());
		simpleStateContainer.changeState(initial -> initial + "-changed");
		Assertions.assertEquals(1, simpleStateContainer.getStateChangedEvents().size());
		IStateHasChangedEvent<String> poll = simpleStateContainer.getStateChangedEvents().poll();
		Assertions.assertEquals("init", poll.getSourceState());
		Assertions.assertEquals("init-changed", poll.getTargetState());

	}

}

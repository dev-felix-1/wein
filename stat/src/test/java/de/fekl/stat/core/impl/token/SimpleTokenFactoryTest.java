package de.fekl.stat.core.impl.token;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleTokenFactoryTest {

	@Test
	public void testTokenCreation() {
		var simpleTokenFactory = new SimpleTokenFactory();
		var token = simpleTokenFactory.createToken();
		Assertions.assertNotNull(token);
		Assertions.assertNotNull(token.getId());

		token = simpleTokenFactory.createToken("tid");
		Assertions.assertNotNull(token);
		Assertions.assertEquals("tid", token.getId());
	}

	@Test
	public void testTokenCopy() {
		var simpleTokenFactory = new SimpleTokenFactory();
		var token = simpleTokenFactory.createToken();
		var tokenCopy = simpleTokenFactory.copyToken(token);
		Assertions.assertNotNull(tokenCopy);
		Assertions.assertNotEquals(token.getId(), tokenCopy.getId());
	}
	
	@Test
	public void testTokenMerge() {
		var simpleTokenFactory = new SimpleTokenFactory();
		var token1 = simpleTokenFactory.createToken();
		var token2 = simpleTokenFactory.createToken();
		
		var tokenMerge = simpleTokenFactory.mergeToken(Arrays.asList(token1,token2));
		Assertions.assertNotNull(tokenMerge);
		Assertions.assertNotEquals(tokenMerge.getId(), token1.getId());
		Assertions.assertNotEquals(tokenMerge.getId(), token2.getId());
		
		tokenMerge = simpleTokenFactory.mergeToken(token1,token2);
		Assertions.assertNotNull(tokenMerge);
		Assertions.assertNotEquals(tokenMerge.getId(), token1.getId());
		Assertions.assertNotEquals(tokenMerge.getId(), token2.getId());
		
		tokenMerge = simpleTokenFactory.mergeToken(token1);
		Assertions.assertNotNull(tokenMerge);
		Assertions.assertEquals(tokenMerge.getId(), token1.getId());
	}

}

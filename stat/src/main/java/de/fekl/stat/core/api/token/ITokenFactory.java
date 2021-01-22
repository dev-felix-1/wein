package de.fekl.stat.core.api.token;

import java.util.Arrays;
import java.util.List;

import de.fekl.dine.util.Precondition;

/**
 * 
 * @author <a href="mailto:kleinfhq@gmail.com">Felix Kleine-Wilde</a>
 *
 * @since 1.0.0
 *
 * @param <T> the kind of token that is produced
 */
public interface ITokenFactory<T extends IToken> {

	T createToken(String id);

	default T createToken() {
		return createToken(TokenNames.generateTokenName());
	}

	/**
	 * 
	 * @param token to copy
	 * @return new token Object with same information except of id. The id has to be
	 *         a new one.
	 */
	T copyToken(T token);

	/**
	 * 
	 * @param tokens to be merged
	 * @return new token Object with merged information of all tokens. The id has to
	 *         be a new one. If only one token is given, the original token is
	 *         returned.
	 */
	T mergeToken(List<T> tokens);

	/**
	 * 
	 * @param tokens to be merged
	 * @return new token Object with merged information of all tokens. The id has to
	 *         be a new one. If only one token is given, the original token is
	 *         returned.
	 */
	@SuppressWarnings({ "varargs", "unchecked" })
	default T mergeToken(T... tokens) {
		Precondition.isNotNull(tokens);
		if (tokens.length == 0) {
			throw new IllegalArgumentException("no tokens to merge");
		}
		return mergeToken(Arrays.asList(tokens));
	}

}

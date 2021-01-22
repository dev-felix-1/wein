package de.fekl.stat.core.api.token;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface ITokenStore<T extends IToken> {

	void putToken(String nodeId, T token);

	T removeToken(String nodeId, String tokenId);

	default T removeToken(String nodeId, T token) {
		return removeToken(nodeId, token.getId());
	}

	Set<T> getTokens(String nodeId);

	default Set<String> getTokenIds(String nodeId) {
		return getTokens(nodeId).stream().map(IToken::getId).collect(Collectors.toSet());
	}

	Map<String, Set<T>> getTokenMapping();

	String getPosition(String tokenId);

	default String getPosition(T token) {
		return getPosition(token.getId());
	}

	Map<String, String> getTokenPositions();

	T getToken(String tokenId);

	void clear();

	public static String print(ITokenStore<?> store) {
		//@formatter:off
		var printTemplate = """
		%s {
		    Tokens {
		%s
		    }
		}
		""";
		return String.format(printTemplate, 
				store.getClass().getSimpleName(), 
				store.getTokenMapping().entrySet().stream()
					.map(entry -> String.format("%-6s %s : %s", "",
							entry.getKey(),
							entry.getValue().stream().map(IToken::toString).collect(Collectors.joining(", ")))
						)
					.collect(Collectors.joining("\n")));
		//@formatter:on
	}

}

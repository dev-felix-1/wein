package de.fekl.dine.api.state;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface ITokenStore<T extends IToken> {
	
	void putToken(String nodeId, T token);
	
	void removeToken(String nodeId, String tokenId);

	Set<T> getTokens(String nodeId);

	Map<String, Set<T>> getTokenMapping();
	
	String getPosition(String tokenId);
	
	Map<String, String> getTokenPositions();
	
	T getToken(String tokenId);
	
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
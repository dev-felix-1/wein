package de.fekl.dine.api.state;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.fekl.baut.Precondition;

public class SimpleTokenStore implements ITokenStore {

	private final Map<String, Set<IToken>> tokenMapping;
	private final Map<String, IToken> tokens;

	public SimpleTokenStore(SimpleTokenStore toCopyFrom) {
		super();
		tokenMapping = new HashMap<>(toCopyFrom.tokenMapping);
		tokens = new HashMap<>(toCopyFrom.tokens);
	}

	public SimpleTokenStore() {
		super();
		tokenMapping = new HashMap<>();
		tokens = new HashMap<>();
	}

	@Override
	public synchronized void putToken(String nodeId, IToken token) {
		Precondition.isNotEmpty(nodeId);
		Precondition.isNotNull(token);
		Precondition.isNotEmpty(token.getId());
		if (tokens.containsKey(token.getId())) {
			throw new IllegalArgumentException(String.format("TokenId %s already taken", token.getId()));
		}
		tokens.put(token.getId(), token);
		Set<IToken> tokenSet = tokenMapping.computeIfAbsent(nodeId, (id) -> new HashSet<>());
		tokenSet.add(token);
	}

	@Override
	public Set<IToken> getTokens(String nodeId) {
		Precondition.isNotEmpty(nodeId);
		return Collections.unmodifiableSet(tokenMapping.get(nodeId));
	}

	@Override
	public Map<String, Set<IToken>> getTokenMapping() {
		return Collections.unmodifiableMap(tokenMapping);
	}

	@Override
	public void removeToken(String nodeId, String tokenId) {
		Precondition.isNotEmpty(nodeId);
		Precondition.isNotEmpty(nodeId);
		tokens.remove(tokenId);
		Set<IToken> set = tokenMapping.get(nodeId);
		if (set != null) {
			Optional<IToken> token = set.stream().filter(t -> t.getId().equals(tokenId)).findFirst();
			if (token.isPresent()) {
				set.remove(token.get());
			}
		}
	}

	@Override
	public IToken getToken(String tokenId) {
		return tokens.get(tokenId);
	}

	@Override
	public String getPosition(String tokenId) {
		Precondition.isNotEmpty(tokenId);
		List<Entry<String, Set<IToken>>> collect = tokenMapping.entrySet().stream()
				.filter(entry -> entry.getValue().stream().anyMatch(t -> t.getId().equals(tokenId)))
				.collect(Collectors.toList());
		if (collect.size() > 1) {
			throw new IllegalStateException("there is more than one node containing " + tokenId);
		} else if (collect.size() < 1) {
			return null;
		}
		return collect.get(0).getKey();
	}

	@Override
	public Map<String, String> getTokenPositions() {
		return tokens.entrySet().stream().collect(Collectors
				.<Entry<String, IToken>, String, String>toMap(e -> e.getKey(), e -> getPosition(e.getValue().getId())));
	}
}

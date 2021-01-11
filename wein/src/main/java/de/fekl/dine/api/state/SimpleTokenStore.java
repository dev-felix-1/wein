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

public class SimpleTokenStore<T extends IToken> implements ITokenStore<T> {

	private final Map<String, Set<T>> tokenMapping;
	private final Map<String, T> tokens;

	public SimpleTokenStore(SimpleTokenStore<T> toCopyFrom) {
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
	public synchronized void putToken(String nodeId, T token) {
		Precondition.isNotEmpty(nodeId);
		Precondition.isNotNull(token);
		Precondition.isNotEmpty(token.getId());
		if (tokens.containsKey(token.getId())) {
			throw new IllegalArgumentException(String.format("TokenId %s already taken", token.getId()));
		}
		tokens.put(token.getId(), token);
		Set<T> tokenSet = tokenMapping.computeIfAbsent(nodeId, (id) -> new HashSet<>());
		tokenSet.add(token);
	}

	@Override
	public Set<T> getTokens(String nodeId) {
		Precondition.isNotEmpty(nodeId);
		return Collections.unmodifiableSet(tokenMapping.get(nodeId));
	}

	@Override
	public Map<String, Set<T>> getTokenMapping() {
		return Collections.unmodifiableMap(tokenMapping);
	}

	@Override
	public void removeToken(String nodeId, String tokenId) {
		Precondition.isNotEmpty(nodeId);
		Precondition.isNotEmpty(nodeId);
		tokens.remove(tokenId);
		Set<T> set = tokenMapping.get(nodeId);
		if (set != null) {
			Optional<T> token = set.stream().filter(t -> t.getId().equals(tokenId)).findFirst();
			if (token.isPresent()) {
				set.remove(token.get());
			}
		}
	}

	@Override
	public T getToken(String tokenId) {
		return tokens.get(tokenId);
	}

	@Override
	public String getPosition(String tokenId) {
		Precondition.isNotEmpty(tokenId);
		List<Entry<String, Set<T>>> collect = tokenMapping.entrySet().stream()
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
		return tokens.entrySet().stream().collect(Collectors.<Entry<String, T>, String, String>toMap(e -> e.getKey(),
				e -> getPosition(e.getValue().getId())));
	}
}
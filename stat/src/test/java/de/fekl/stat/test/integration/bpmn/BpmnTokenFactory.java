package de.fekl.stat.test.integration.bpmn;

import java.util.List;
import java.util.stream.Collectors;

import de.fekl.stat.core.api.token.IToken;
import de.fekl.stat.core.api.token.ITokenFactory;
import de.fekl.stat.core.api.token.TokenNames;

public class BpmnTokenFactory implements ITokenFactory<IBpmnToken>{

	@Override
	public IBpmnToken createToken(String id) {
		return new BpmnToken(id);
	}

	@Override
	public IBpmnToken copyToken(IBpmnToken token) {
		var copy = new BpmnToken(TokenNames.generateTokenName() + "_copy:" + token.getId());
		token.getAll().forEach((k,v)->copy.set(k,v));
		return copy;
	}

	@Override
	public IBpmnToken mergeToken(List<IBpmnToken> tokens) {
		if (tokens.size() == 1) {
			return tokens.get(0);
		}
		return new BpmnToken(tokens.stream().map(IToken::getId).collect(Collectors.joining("+")));
	}


}

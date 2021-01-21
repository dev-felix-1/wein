package de.fekl.tran.impl;

import de.fekl.dine.util.AbstractRegistry;
import de.fekl.tran.api.core.ITransformer;
import de.fekl.tran.api.core.ITransformerRegistry;

public class SimpleTransformerRegistry extends AbstractRegistry<String, ITransformer<?,?>> implements ITransformerRegistry {

}

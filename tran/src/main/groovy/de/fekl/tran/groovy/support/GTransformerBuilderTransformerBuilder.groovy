package de.fekl.tran.groovy.support

import de.fekl.tran.api.core.IContentType
import de.fekl.tran.api.core.IMerge
import de.fekl.tran.api.core.ITransformer
import de.fekl.tran.impl.MergerBuilder
import de.fekl.tran.impl.StandardContentTypes
import de.fekl.tran.impl.TransformerBuilder

class GTransformerBuilderTransformerBuilder extends TransformerBuilder {
	
	boolean merger = false
	List<IContentType> sourceContentTypes = []
	
	@Override
	public TransformerBuilder source(IContentType sourceContentType) {
		sourceContentTypes+=sourceContentType
		return this
	}
	
	@Override
	public TransformerBuilder source(String sourceContentType) {
		sourceContentTypes+=StandardContentTypes.byName(sourceContentType)
		return this
	}
	
	public ITransformer build() {
		if (merger) {
			def mergerBuilder = new MergerBuilder()
			mergerBuilder.autoLookUp = autoLookUp
			mergerBuilder.autoRegister = autoRegister
			mergerBuilder.autoSplit = autoSplit
			mergerBuilder.id(id)
			mergerBuilder.registry = registry
			mergerBuilder.sourceContentTypes = sourceContentTypes
			mergerBuilder.targetContentType = targetContentType
			mergerBuilder.transformation = transformation as IMerge
			return mergerBuilder.build()
		} else {
			super.source(sourceContentTypes.last())
			return super.build()
		}
	}
}

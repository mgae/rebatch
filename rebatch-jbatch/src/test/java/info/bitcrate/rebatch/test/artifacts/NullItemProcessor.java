package info.bitcrate.rebatch.test.artifacts;

import javax.batch.api.chunk.ItemProcessor;

public class NullItemProcessor implements ItemProcessor {

	@Override
	public Object processItem(Object item) throws Exception {
		return item;
	}

}

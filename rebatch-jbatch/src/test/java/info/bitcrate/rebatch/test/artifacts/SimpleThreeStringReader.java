package info.bitcrate.rebatch.test.artifacts;

import javax.batch.api.chunk.AbstractItemReader;

public class SimpleThreeStringReader extends AbstractItemReader {

	int count = 0;
	
	@Override
	public Object readItem() throws Exception {
		
		if (++count > 3) {
			return null;
		}
		
		return String.valueOf(count);
	}
}

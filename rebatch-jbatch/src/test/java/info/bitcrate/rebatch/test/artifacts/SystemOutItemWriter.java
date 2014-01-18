package info.bitcrate.rebatch.test.artifacts;

import java.util.List;

import javax.batch.api.chunk.AbstractItemWriter;

public class SystemOutItemWriter extends AbstractItemWriter {

	@Override
	public void writeItems(List<Object> items) throws Exception {
		for (Object item : items) {
			System.out.println(item);
		}
	}
}

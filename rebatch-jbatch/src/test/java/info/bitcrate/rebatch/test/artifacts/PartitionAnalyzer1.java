package info.bitcrate.rebatch.test.artifacts;

import javax.batch.api.partition.AbstractPartitionAnalyzer;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

public class PartitionAnalyzer1 extends AbstractPartitionAnalyzer {
	
	@Inject
	private StepContext stepContext;
	
	@Override
	public void analyzeStatus(BatchStatus batchStatus, String exitStatus)
			throws Exception {
		System.out.println("batchStatus: " + batchStatus + "; " + exitStatus);
		
		stepContext.setExitStatus(exitStatus);
	}
}

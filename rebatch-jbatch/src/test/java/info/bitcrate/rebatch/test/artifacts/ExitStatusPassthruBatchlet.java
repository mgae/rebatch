package info.bitcrate.rebatch.test.artifacts;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

public class ExitStatusPassthruBatchlet extends AbstractBatchlet {

	@Inject
	private StepContext stepContext;

	@Inject
	@BatchProperty(name = "exitStatus")
	private String exitStatus;
	
	@Override
	public String process() throws Exception {
		System.out.println("Exit status: " + exitStatus);
		stepContext.setExitStatus(String.valueOf(exitStatus));
		
		return String.valueOf(exitStatus);
	}
}

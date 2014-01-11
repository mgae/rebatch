package info.bitcrate.rebatch.test.artifacts;

import java.util.Properties;

import javax.batch.api.BatchProperty;
import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.inject.Inject;

public class PartitionMapper1 implements PartitionMapper {

	@Inject
	@BatchProperty(name = "jobParam")
	private String aJobParameter = "A";
	
	@Inject
	@BatchProperty(name = "jobProp")
	private String aJobProperty = "A";
	
	/*@Inject
	@BatchProperty(name = "stepProp")
	private String aStepProperty = "A";*/
	
	@Override
	public PartitionPlan mapPartitions() throws Exception {
		
		PartitionPlan plan = new PartitionPlanImpl();
		
		Properties[] properties = new Properties[1];
		properties[0] = new Properties();
		properties[0].put("exitStatus", aJobParameter + aJobProperty + "Z");

		plan.setPartitionProperties(properties);
		plan.setPartitions(1);
		plan.setThreads(1);
		
		return plan;
	}

}

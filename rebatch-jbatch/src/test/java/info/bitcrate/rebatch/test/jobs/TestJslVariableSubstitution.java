package info.bitcrate.rebatch.test.jobs;

import info.bitcrate.rebatch.util.Batches;

import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestJslVariableSubstitution {

	JobOperator operator = BatchRuntime.getJobOperator();
	
	@BeforeClass
	public static void initialize() {
	}
	
	@Test
	public void testPartitionMapperPropertyInjection() {
		Properties parameters = new Properties();
		parameters.put("jobParam", "Z");
		
		long executionId = operator.start("partition-mapper-properties-test", parameters);
		
		Batches.waitForEnd(executionId);
		
		Assert.assertEquals(
				BatchStatus.FAILED, 
				operator.getJobExecution(executionId).getBatchStatus());
	}

	@Test
	public void testVariableInheritance() {
		long executionId = operator.start("variable-inheritance-test", null);
		
		Batches.waitForEnd(executionId);
		
		Assert.assertEquals(
				"FAILED_NULL", 
				operator.getJobExecution(executionId).getExitStatus());
	}
}

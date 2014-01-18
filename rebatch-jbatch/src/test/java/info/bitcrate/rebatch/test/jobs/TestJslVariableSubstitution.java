package info.bitcrate.rebatch.test.jobs;

import info.bitcrate.rebatch.util.Batches;

import java.util.List;
import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.Metric;
import javax.batch.runtime.StepExecution;
import javax.batch.runtime.Metric.MetricType;

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

	@Test
	public void testMetricsAggregation() {
		long executionId = operator.start("metrics-aggregation-test", null);
		
		Batches.waitForEnd(executionId);
		
		List<StepExecution> steps = operator.getStepExecutions(executionId);
		
		Assert.assertEquals(1L, steps.size());
		
		StepExecution step = steps.get(0);
		
		for (Metric m : step.getMetrics()) {
			if (m.getType() == MetricType.WRITE_COUNT) {
				Assert.assertEquals(6, m.getValue());
			}
		}
	}
}

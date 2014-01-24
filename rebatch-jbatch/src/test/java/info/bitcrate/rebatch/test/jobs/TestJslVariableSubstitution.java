package info.bitcrate.rebatch.test.jobs;

import info.bitcrate.rebatch.util.Batches;

import java.util.List;
import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobInstance;
import javax.batch.runtime.Metric;
import javax.batch.runtime.Metric.MetricType;
import javax.batch.runtime.StepExecution;

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
	
	@Test
	public void testJobContextSubstitution() {
		long executionId = operator.start("job-context-substitution-test", null);
		JobInstance instance = operator.getJobInstance(executionId);
		
		Batches.waitForEnd(executionId);
		
		Assert.assertEquals(
				executionId + "_" + instance.getInstanceId() + "_" + instance.getJobName(), 
				operator.getJobExecution(executionId).getExitStatus());
	}
	
	@Test
	public void testStepContextOutOfScope() {
		long executionId = operator.start("step-context-out-of-scope-test", null);

		Batches.waitForEnd(executionId);
		
		Assert.assertEquals(
				"FAILED", 
				operator.getJobExecution(executionId).getExitStatus());
	}
	
	@Test
	public void testStepContextSubstitution() {
		long executionId = operator.start("step-context-substitution-test", null);

		Batches.waitForEnd(executionId);
		
		List<StepExecution> steps = operator.getStepExecutions(executionId);
		StepExecution step1 = steps.get(0);
		
		Assert.assertEquals(
				executionId + "_" + step1.getStepExecutionId() + "_" + step1.getStepName(), 
				operator.getJobExecution(executionId).getExitStatus());
	}
}

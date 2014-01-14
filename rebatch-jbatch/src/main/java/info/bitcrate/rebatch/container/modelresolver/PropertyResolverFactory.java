/*
 * Copyright 2012 International Business Machines Corp.
 * 
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package info.bitcrate.rebatch.container.modelresolver;


import info.bitcrate.rebatch.container.jsl.TransitionElement;
import info.bitcrate.rebatch.container.modelresolver.impl.AnalyzerPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.BatchletPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.CheckpointAlgorithmPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.ChunkPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.CollectorPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.ControlElementPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.DecisionPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.ExceptionClassesPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.FlowPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.ItemProcessorPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.ItemReaderPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.ItemWriterPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.JobPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.ListenerPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.PartitionMapperPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.PartitionPlanPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.PartitionPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.PartitionReducerPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.SplitPropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.impl.StepPropertyResolver;
import info.bitcrate.rebatch.jaxb.Analyzer;
import info.bitcrate.rebatch.jaxb.Batchlet;
import info.bitcrate.rebatch.jaxb.Chunk;
import info.bitcrate.rebatch.jaxb.Collector;
import info.bitcrate.rebatch.jaxb.Decision;
import info.bitcrate.rebatch.jaxb.ExceptionClassFilter;
import info.bitcrate.rebatch.jaxb.Flow;
import info.bitcrate.rebatch.jaxb.ItemProcessor;
import info.bitcrate.rebatch.jaxb.ItemReader;
import info.bitcrate.rebatch.jaxb.ItemWriter;
import info.bitcrate.rebatch.jaxb.JSLJob;
import info.bitcrate.rebatch.jaxb.Listener;
import info.bitcrate.rebatch.jaxb.Partition;
import info.bitcrate.rebatch.jaxb.PartitionMapper;
import info.bitcrate.rebatch.jaxb.PartitionPlan;
import info.bitcrate.rebatch.jaxb.PartitionReducer;
import info.bitcrate.rebatch.jaxb.Split;
import info.bitcrate.rebatch.jaxb.Step;

public class PropertyResolverFactory {
	/*public static <T, R extends PropertyResolver<T>> R newInstance(
			Class<T> klass, 
			boolean isPartitionedStep) {
		
		if (klass.equals(JSLJob.class)) {
			return (R) new JobPropertyResolver(isPartitionedStep);
		} else if (klass.equals(Step.class)) {
			
		}
			
		return null;
	}*/
	
    public static PropertyResolver<JSLJob> createJobPropertyResolver(boolean isPartitionedStep) {
        return new JobPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<Step> createStepPropertyResolver(boolean isPartitionedStep) {
        return new StepPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<Batchlet> createBatchletPropertyResolver(boolean isPartitionedStep) {
        return new BatchletPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<Split> createSplitPropertyResolver(boolean isPartitionedStep) {
        return new SplitPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<Flow> createFlowPropertyResolver(boolean isPartitionedStep) {
        return new FlowPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<Chunk> createChunkPropertyResolver(boolean isPartitionedStep) {
        return new ChunkPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<TransitionElement> createTransitionElementPropertyResolver(boolean isPartitionedStep) {
        return new ControlElementPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<Decision> createDecisionPropertyResolver(boolean isPartitionedStep) {
        return new DecisionPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<Listener> createListenerPropertyResolver(boolean isPartitionedStep) {
        return new ListenerPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<Partition> createPartitionPropertyResolver(boolean isPartitionedStep) {
        return new PartitionPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<PartitionMapper> createPartitionMapperPropertyResolver(boolean isPartitionedStep) {
        return new PartitionMapperPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<PartitionPlan> createPartitionPlanPropertyResolver(boolean isPartitionedStep) {
        return new PartitionPlanPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<PartitionReducer> createPartitionReducerPropertyResolver(boolean isPartitionedStep) {
        return new PartitionReducerPropertyResolver(isPartitionedStep);
    }

    public static CheckpointAlgorithmPropertyResolver createCheckpointAlgorithmPropertyResolver(boolean isPartitionedStep) {
        return new CheckpointAlgorithmPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<Collector> createCollectorPropertyResolver(boolean isPartitionedStep) {
        return new CollectorPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<Analyzer> createAnalyzerPropertyResolver(boolean isPartitionedStep) {
        return new AnalyzerPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<ItemReader> createReaderPropertyResolver(boolean isPartitionedStep) {
        return new ItemReaderPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<ItemProcessor> createProcessorPropertyResolver(boolean isPartitionedStep) {
        return new ItemProcessorPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<ItemWriter> createWriterPropertyResolver(boolean isPartitionedStep) {
        return new ItemWriterPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<ExceptionClassFilter> createSkippableExceptionClassesPropertyResolver(boolean isPartitionedStep) {
        return new ExceptionClassesPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<ExceptionClassFilter> createRetryableExceptionClassesPropertyResolver(boolean isPartitionedStep) {
        return new ExceptionClassesPropertyResolver(isPartitionedStep);
    }

    public static PropertyResolver<ExceptionClassFilter> createNoRollbackExceptionClassesPropertyResolver(boolean isPartitionedStep) {
        return new ExceptionClassesPropertyResolver(isPartitionedStep);
    }

    private PropertyResolverFactory() {
        // no-op
    }
}

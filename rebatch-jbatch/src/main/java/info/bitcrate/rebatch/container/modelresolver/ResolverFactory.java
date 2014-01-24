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
import info.bitcrate.rebatch.container.modelresolver.impl.AbstractPropertyResolver;
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
import info.bitcrate.rebatch.jaxb.CheckpointAlgorithm;
import info.bitcrate.rebatch.jaxb.Chunk;
import info.bitcrate.rebatch.jaxb.Collector;
import info.bitcrate.rebatch.jaxb.Decision;
import info.bitcrate.rebatch.jaxb.End;
import info.bitcrate.rebatch.jaxb.ExceptionClassFilter;
import info.bitcrate.rebatch.jaxb.Fail;
import info.bitcrate.rebatch.jaxb.Flow;
import info.bitcrate.rebatch.jaxb.ItemProcessor;
import info.bitcrate.rebatch.jaxb.ItemReader;
import info.bitcrate.rebatch.jaxb.ItemWriter;
import info.bitcrate.rebatch.jaxb.JSLJob;
import info.bitcrate.rebatch.jaxb.Listener;
import info.bitcrate.rebatch.jaxb.Next;
import info.bitcrate.rebatch.jaxb.Partition;
import info.bitcrate.rebatch.jaxb.PartitionMapper;
import info.bitcrate.rebatch.jaxb.PartitionPlan;
import info.bitcrate.rebatch.jaxb.PartitionReducer;
import info.bitcrate.rebatch.jaxb.Split;
import info.bitcrate.rebatch.jaxb.Step;
import info.bitcrate.rebatch.jaxb.Stop;

import java.util.HashMap;
import java.util.Map;

public class ResolverFactory {
	
	private static final Map<Class<?>, AbstractPropertyResolver<?>> RESOLVERS = 
			new HashMap<Class<?>, AbstractPropertyResolver<?>>();
	
	static {
		RESOLVERS.put(JSLJob.class, new JobPropertyResolver());
		RESOLVERS.put(Step.class, new StepPropertyResolver());
		RESOLVERS.put(Batchlet.class, new BatchletPropertyResolver());
		RESOLVERS.put(Flow.class, new FlowPropertyResolver());
		RESOLVERS.put(Split.class, new SplitPropertyResolver());
		RESOLVERS.put(Chunk.class, new ChunkPropertyResolver());
		RESOLVERS.put(TransitionElement.class, new ControlElementPropertyResolver());
		RESOLVERS.put(End.class, new ControlElementPropertyResolver());
		RESOLVERS.put(Fail.class, new ControlElementPropertyResolver());
		RESOLVERS.put(Next.class, new ControlElementPropertyResolver());
		RESOLVERS.put(Stop.class, new ControlElementPropertyResolver());

		
		RESOLVERS.put(Decision.class, new DecisionPropertyResolver());
		RESOLVERS.put(Listener.class, new ListenerPropertyResolver());
		RESOLVERS.put(Partition.class, new PartitionPropertyResolver());
		RESOLVERS.put(PartitionMapper.class, new PartitionMapperPropertyResolver());
		RESOLVERS.put(PartitionPlan.class, new PartitionPlanPropertyResolver());
		RESOLVERS.put(PartitionReducer.class, new PartitionReducerPropertyResolver());
		RESOLVERS.put(CheckpointAlgorithm.class, new CheckpointAlgorithmPropertyResolver());
		RESOLVERS.put(Collector.class, new CollectorPropertyResolver());
		RESOLVERS.put(Analyzer.class, new AnalyzerPropertyResolver());
		RESOLVERS.put(ItemReader.class, new ItemReaderPropertyResolver());
		RESOLVERS.put(ItemProcessor.class, new ItemProcessorPropertyResolver());
		RESOLVERS.put(ItemWriter.class, new ItemWriterPropertyResolver());
		RESOLVERS.put(ExceptionClassFilter.class, new ExceptionClassesPropertyResolver());
	}
	
	private ResolverFactory() {
	}
	
	@SuppressWarnings("unchecked")
	public static <B, R extends PropertyResolver<B>> R newInstance(
			Class<B> klass, 
			boolean isPartitionedStep) {
		
		AbstractPropertyResolver<B> resolver = 
				(AbstractPropertyResolver<B>) RESOLVERS.get(klass);
		
		if (resolver != null) {
			resolver = resolver.clone();
			resolver.setPartitionedStep(isPartitionedStep);
		}
			
		return (R) resolver;
	}
}

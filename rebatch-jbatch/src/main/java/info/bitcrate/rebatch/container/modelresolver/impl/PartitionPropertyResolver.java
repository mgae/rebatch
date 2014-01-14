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
package info.bitcrate.rebatch.container.modelresolver.impl;

import info.bitcrate.rebatch.container.exception.JobSpecificationException;
import info.bitcrate.rebatch.container.modelresolver.PropertyResolverFactory;
import info.bitcrate.rebatch.jaxb.Partition;
import info.bitcrate.rebatch.jaxb.PartitionMapper;

import java.util.List;
import java.util.Properties;

public class PartitionPropertyResolver extends
		AbstractPropertyResolver<Partition> {

	public PartitionPropertyResolver(boolean isPartitionStep) {
		super(isPartitionStep);
	}

	@Override
	public Partition resolve(Partition partition, List<Properties> properties) {
		
		PartitionMapper mapper = partition.getMapper();
		
		if (mapper != null) {
			PropertyResolverFactory.createPartitionMapperPropertyResolver(isPartitionedStep).resolve(mapper, properties);
		}

		if (partition.getPlan() != null) {
			if (mapper != null) {
				throw new JobSpecificationException("Partition 'plan' element is mutually exclusive with 'mapper' element");
			}
			
			PropertyResolverFactory.createPartitionPlanPropertyResolver(isPartitionedStep).resolve(partition.getPlan(), properties);
		}
		
		if (partition.getCollector() != null) {
			PropertyResolverFactory.createCollectorPropertyResolver(isPartitionedStep).resolve(partition.getCollector(), properties);
		}
		
		if (partition.getAnalyzer() != null) {
			PropertyResolverFactory.createAnalyzerPropertyResolver(isPartitionedStep).resolve(partition.getAnalyzer(), properties);
		}
		
		if (partition.getReducer() != null) {
			PropertyResolverFactory.createPartitionReducerPropertyResolver(isPartitionedStep).resolve(partition.getReducer(), properties);
		}
		
		return partition;
	}
}

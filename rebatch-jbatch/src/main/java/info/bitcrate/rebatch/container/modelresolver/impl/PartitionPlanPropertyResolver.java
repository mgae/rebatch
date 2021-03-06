/**
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

import info.bitcrate.rebatch.jaxb.JSLProperties;
import info.bitcrate.rebatch.jaxb.PartitionPlan;

import java.util.List;
import java.util.Properties;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

public class PartitionPlanPropertyResolver 
	extends AbstractPropertyResolver<PartitionPlan> {

	/*public PartitionPlanPropertyResolver(boolean isPartitionStep) {
		super(isPartitionStep);
	}*/

	@Override
	public PartitionPlan resolve(
			PartitionPlan plan, 
			List<Properties> properties) {

		plan.setPartitions(resolveReferences(plan.getPartitions(), properties));
		plan.setThreads(resolveReferences(plan.getThreads(), properties));

		for (JSLProperties jslProperties : plan.getProperties()) {
			String partition = jslProperties.getPartition(); 

			if (partition != null) {
				jslProperties.setPartition(resolveReferences(partition, properties));
			}
			
			resolveJSLProperties(jslProperties, properties);
		}

		return plan;
	}
	
	@Override
	public PartitionPlan resolve(PartitionPlan plan, JobContext jobContext) {
		plan.setPartitions(resolveReferences(plan.getPartitions(), jobContext));
		plan.setThreads(resolveReferences(plan.getThreads(), jobContext));

		for (JSLProperties jslProperties : plan.getProperties()) {
			String partition = jslProperties.getPartition(); 

			if (partition != null) {
				jslProperties.setPartition(resolveReferences(partition, jobContext));
			}
			
			resolveJSLProperties(jslProperties, jobContext);
		}

		return plan;
	}
	
	@Override
	public PartitionPlan resolve(PartitionPlan plan, StepContext stepContext) {
		plan.setPartitions(resolveReferences(plan.getPartitions(), stepContext));
		plan.setThreads(resolveReferences(plan.getThreads(), stepContext));

		for (JSLProperties jslProperties : plan.getProperties()) {
			String partition = jslProperties.getPartition(); 

			if (partition != null) {
				jslProperties.setPartition(resolveReferences(partition, stepContext));
			}
			
			resolveJSLProperties(jslProperties, stepContext);
		}

		return plan;
	}
}

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

import info.bitcrate.rebatch.jaxb.Partition;

import java.util.List;
import java.util.Properties;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

public class PartitionPropertyResolver extends
		AbstractPropertyResolver<Partition> {

	@Override
	public Partition resolve(Partition partition, List<Properties> properties) {
		_resolve(partition.getMapper(), properties);
		_resolve(partition.getPlan(), properties);
		_resolve(partition.getCollector(), properties);
		_resolve(partition.getAnalyzer(), properties);
		_resolve(partition.getReducer(), properties);
		
		return partition;
	}
	
	@Override
	public Partition resolve(Partition partition, JobContext jobContext) {
		_resolve(partition.getMapper(), jobContext);
		_resolve(partition.getPlan(), jobContext);
		_resolve(partition.getCollector(), jobContext);
		_resolve(partition.getAnalyzer(), jobContext);
		_resolve(partition.getReducer(), jobContext);
		
		return partition;
	}
	
	@Override
	public Partition resolve(Partition partition, StepContext stepContext) {
		_resolve(partition.getMapper(), stepContext);
		_resolve(partition.getPlan(), stepContext);
		_resolve(partition.getCollector(), stepContext);
		_resolve(partition.getAnalyzer(), stepContext);
		_resolve(partition.getReducer(), stepContext);
		
		return partition;
	}
}

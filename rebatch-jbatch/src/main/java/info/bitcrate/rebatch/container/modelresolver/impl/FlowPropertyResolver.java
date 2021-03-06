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

import info.bitcrate.rebatch.jaxb.Flow;

import java.util.List;
import java.util.Properties;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

public class FlowPropertyResolver extends AbstractPropertyResolver<Flow> {

    @Override
    public Flow resolve(Flow flow, List<Properties> properties) {
        flow.setId(resolveReferences(flow.getId(), properties));
        flow.setNextFromAttribute(resolveReferences(flow.getNextFromAttribute(), properties));
       	_resolve(flow.getExecutionElements(), properties);

        return flow;
    }
    
    @Override
    public Flow resolve(Flow flow, JobContext jobContext) {
        flow.setId(resolveReferences(flow.getId(), jobContext));
        flow.setNextFromAttribute(resolveReferences(flow.getNextFromAttribute(), jobContext));
       	_resolve(flow.getExecutionElements(), jobContext);

        return flow;
	}
    
    @Override
    public Flow resolve(Flow flow, StepContext stepContext) {
        flow.setId(resolveReferences(flow.getId(), (StepContext) null));
        flow.setNextFromAttribute(resolveReferences(flow.getNextFromAttribute(), (StepContext) null));
       	_resolve(flow.getExecutionElements(), stepContext);

        return flow;
    }
}

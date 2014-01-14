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

import info.bitcrate.rebatch.container.jsl.ExecutionElement;
import info.bitcrate.rebatch.container.modelresolver.PropertyResolverFactory;
import info.bitcrate.rebatch.jaxb.Decision;
import info.bitcrate.rebatch.jaxb.Flow;
import info.bitcrate.rebatch.jaxb.Split;
import info.bitcrate.rebatch.jaxb.Step;

import java.util.List;
import java.util.Properties;

public class FlowPropertyResolver extends AbstractPropertyResolver<Flow> {

    public FlowPropertyResolver(boolean isPartitionStep) {
        super(isPartitionStep);
    }

    @Override
    public Flow resolve(Flow flow, List<Properties> properties) {
        flow.setId(resolveReferences(flow.getId(), properties));
        flow.setNextFromAttribute(resolveReferences(flow.getNextFromAttribute(), properties));

        for (final ExecutionElement next : flow.getExecutionElements()) {
            if (next instanceof Step) {
                PropertyResolverFactory.createStepPropertyResolver(isPartitionedStep).resolve((Step) next, properties);
            } else if (next instanceof Decision) {
                PropertyResolverFactory.createDecisionPropertyResolver(isPartitionedStep).resolve((Decision) next, properties);
            } else if (next instanceof Flow) {
                PropertyResolverFactory.createFlowPropertyResolver(isPartitionedStep).resolve((Flow) next, properties);
            } else if (next instanceof Split) {
                PropertyResolverFactory.createSplitPropertyResolver(isPartitionedStep).resolve((Split) next, properties);
            }
        }

        return flow;
    }
}

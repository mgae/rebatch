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

import info.bitcrate.rebatch.container.modelresolver.PropertyResolverFactory;
import info.bitcrate.rebatch.jaxb.Flow;
import info.bitcrate.rebatch.jaxb.Split;

import java.util.List;
import java.util.Properties;

public class SplitPropertyResolver extends AbstractPropertyResolver<Split> {


    public SplitPropertyResolver(boolean isPartitionStep) {
        super(isPartitionStep);
    }

    @Override
    public Split resolve(Split split, List<Properties> properties) {
        split.setId(resolveReferences(split.getId(), properties));
        split.setNextFromAttribute(resolveReferences(split.getNextFromAttribute(), properties));

        // Resolve all the properties defined for this step
        for (Flow flow : split.getFlows()) {
            PropertyResolverFactory.createFlowPropertyResolver(this.isPartitionedStep).resolve(flow, properties);
        }
        
    	return split;
    }
}

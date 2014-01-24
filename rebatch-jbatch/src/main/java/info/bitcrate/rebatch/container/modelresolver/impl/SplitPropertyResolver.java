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

import info.bitcrate.rebatch.jaxb.Split;

import java.util.List;
import java.util.Properties;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

public class SplitPropertyResolver extends AbstractPropertyResolver<Split> {

    @Override
    public Split resolve(Split split, List<Properties> properties) {
        split.setId(resolveReferences(split.getId(), properties));
        split.setNextFromAttribute(resolveReferences(split.getNextFromAttribute(), properties));
        _resolve(split.getFlows(), properties);
        
    	return split;
    }
    
    @Override
    public Split resolve(Split split, JobContext jobContext) {
        split.setId(resolveReferences(split.getId(), jobContext));
        split.setNextFromAttribute(resolveReferences(split.getNextFromAttribute(), jobContext));
        _resolve(split.getFlows(), jobContext);
        
    	return split;
    }
    
    @Override
    public Split resolve(Split split, StepContext stepContext) {
        split.setId(resolveReferences(split.getId(), (StepContext) null));
        split.setNextFromAttribute(resolveReferences(split.getNextFromAttribute(), (StepContext) null));
        _resolve(split.getFlows(), stepContext);
        
    	return split;
    }
}

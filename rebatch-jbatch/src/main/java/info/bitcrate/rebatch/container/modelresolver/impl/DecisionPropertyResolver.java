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

import info.bitcrate.rebatch.jaxb.Decision;

import java.util.List;
import java.util.Properties;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

public class DecisionPropertyResolver extends AbstractPropertyResolver<Decision> {

    public Decision resolve(Decision decision, List<Properties> properties) {
        decision.setId(resolveReferences(decision.getId(), properties));
        decision.setRef(resolveReferences(decision.getRef(), properties));

      	resolveJSLProperties(decision.getProperties(), properties);
       	_resolve(decision.getTransitionElements(), properties);

        return decision;
    }
    
    @Override
    public Decision resolve(Decision decision, JobContext jobContext) {
        decision.setId(resolveReferences(decision.getId(), jobContext));
        decision.setRef(resolveReferences(decision.getRef(), jobContext));

      	resolveJSLProperties(decision.getProperties(), jobContext);
       	_resolve(decision.getTransitionElements(), jobContext);

        return decision;
    }
    
    @Override
    public Decision resolve(Decision decision, StepContext stepContext) {
        decision.setId(resolveReferences(decision.getId(), (StepContext) null));
        decision.setRef(resolveReferences(decision.getRef(), (StepContext) null));

      	resolveJSLProperties(decision.getProperties(), (StepContext) null);
       	_resolve(decision.getTransitionElements(), (StepContext) null);

        return decision;
    }
}

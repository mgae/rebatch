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

import info.bitcrate.rebatch.jaxb.JSLProperties;
import info.bitcrate.rebatch.jaxb.Partition;
import info.bitcrate.rebatch.jaxb.Step;

import java.util.List;
import java.util.Properties;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

public class StepPropertyResolver extends AbstractPropertyResolver<Step> {

    /*public StepPropertyResolver(boolean isPartitionedStep) {
        super(isPartitionedStep);
    }*/

    @Override
    public Step resolve(Step step, List<Properties> properties) {
    	
        step.setId(resolveReferences(step.getId(), properties));
        step.setAllowStartIfComplete(resolveReferences(step.getAllowStartIfComplete(), properties));
        step.setNextFromAttribute(resolveReferences(step.getNextFromAttribute(), properties));
        step.setStartLimit(resolveReferences(step.getStartLimit(), properties));
    	
    	JSLProperties jslProperties = step.getProperties();
    	Properties stepProperties = new Properties();
    	properties.add(stepProperties);
    	
    	if (jslProperties != null) {
    		resolveJSLProperties(jslProperties, properties, stepProperties);
    	} 
    	
        Partition partition = step.getPartition();
        
        if (partition != null) {
        	/*-
        	 * Add a dummy properties map here so that it can be referenced 
        	 * later for validation purposes. Property resolution will not 
        	 * occur if this is not running during the partitioned step phase.
        	 */
        	if (!isPartitionedStep) {
        		properties.set(PROPERTY_PARTITION, new Properties());
        	}
        	
        	_resolve(partition, properties);
        }
        
    	if (step.getListeners() != null) {
           	_resolve(step.getListeners().getListenerList(), properties);
        }

       	_resolve(step.getTransitionElements(), properties);
       	_resolve(step.getBatchlet(), properties);
       	_resolve(step.getChunk(), properties);
        
    	if (!isPartitionedStep) {
    		properties.set(PROPERTY_PARTITION, null);
    	}
        
    	properties.remove(properties.size() - 1);

    	return step;
    }
    
    @Override
    public Step resolve(Step step, JobContext jobContext) {
    	step.setId(resolveReferences(step.getId(), jobContext));
        step.setAllowStartIfComplete(resolveReferences(step.getAllowStartIfComplete(), jobContext));
        step.setNextFromAttribute(resolveReferences(step.getNextFromAttribute(), jobContext));
        step.setStartLimit(resolveReferences(step.getStartLimit(), jobContext));
    	
    	JSLProperties jslProperties = step.getProperties();
    	
    	if (jslProperties != null) {
    		resolveJSLProperties(jslProperties, jobContext);
    	} 
    	
        Partition partition = step.getPartition();
        
        if (partition != null) {
        	_resolve(partition, jobContext);
        }
        
    	if (step.getListeners() != null) {
           	_resolve(step.getListeners().getListenerList(), jobContext);
        }

       	_resolve(step.getTransitionElements(), jobContext);
       	_resolve(step.getBatchlet(), jobContext);
       	_resolve(step.getChunk(), jobContext);
        
    	return step;
    }
    
    @Override
    public Step resolve(Step step, StepContext stepContext) {
    	if (!step.getId().equals(stepContext.getStepName())) {
    		return step;
    	}
    	
    	step.setId(resolveReferences(step.getId(), stepContext));
        step.setAllowStartIfComplete(resolveReferences(step.getAllowStartIfComplete(), stepContext));
        step.setNextFromAttribute(resolveReferences(step.getNextFromAttribute(), stepContext));
        step.setStartLimit(resolveReferences(step.getStartLimit(), stepContext));
    	
    	JSLProperties jslProperties = step.getProperties();
    	
    	if (jslProperties != null) {
    		resolveJSLProperties(jslProperties, stepContext);
    	} 
    	
        Partition partition = step.getPartition();
        
        if (partition != null) {
        	_resolve(partition, stepContext);
        }
        
    	if (step.getListeners() != null) {
           	_resolve(step.getListeners().getListenerList(), stepContext);
        }

       	_resolve(step.getTransitionElements(), stepContext);
       	_resolve(step.getBatchlet(), stepContext);
       	_resolve(step.getChunk(), stepContext);
        
    	return step;
    }
}

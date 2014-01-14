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
import info.bitcrate.rebatch.container.modelresolver.PropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.PropertyResolverFactory;
import info.bitcrate.rebatch.jaxb.Decision;
import info.bitcrate.rebatch.jaxb.Flow;
import info.bitcrate.rebatch.jaxb.JSLJob;
import info.bitcrate.rebatch.jaxb.JSLProperties;
import info.bitcrate.rebatch.jaxb.Listener;
import info.bitcrate.rebatch.jaxb.Split;
import info.bitcrate.rebatch.jaxb.Step;

import java.util.List;
import java.util.Properties;


public class JobPropertyResolver extends AbstractPropertyResolver<JSLJob> {

    public JobPropertyResolver(boolean isPartitionStep) {
        super(isPartitionStep);
    }

    /**
     * @param job            This method will modify the given job. If you need to hold on
     *                       to the original job you need to create a clone of the job
     *                       before passing it to this method.
     * @param submittedProps The job parameters associated with this job. null is valid if
     *                       no parameters are passed.
     * @param parentProps    Properties that are inherited from parent elements. Job is top
     *                       level element so it can have no parents, so this paramter is
     *                       currently ignored. Null is valid.
     * @return
     */
    @Override
    public JSLJob resolve(JSLJob job, List<Properties> properties) {
        /*-
         * Resolve all the properties used in attributes and update the JAXB
         * model
         */
    	job.setId(resolveReferences(job.getId(), properties));
    	job.setRestartable(resolveReferences(job.getRestartable(), properties));
    	
    	JSLProperties jslProperties = job.getProperties();
    	Properties jobProperties = new Properties();
    	properties.set(PROPERTY_JOB, jobProperties);
    	
    	if (jslProperties != null) {
    		resolveJSLProperties(jslProperties, properties, jobProperties);
    	} 
    	
        // Resolve Listener properties, this is list of listeners List<Listener>
        if (job.getListeners() != null) {
        	PropertyResolver<Listener> resolver = 
        			PropertyResolverFactory.createListenerPropertyResolver(isPartitionedStep);
        	
            for (final Listener listener : job.getListeners().getListenerList()) {
            	resolver.resolve(listener, properties);
            }
        }

        for (final ExecutionElement next : job.getExecutionElements()) {
            if (next instanceof Step) {
                PropertyResolverFactory.createStepPropertyResolver(isPartitionedStep).resolve((Step) next, properties);
            } else if (next instanceof Decision) {
                PropertyResolverFactory.createDecisionPropertyResolver(isPartitionedStep).resolve((Decision) next, properties);
            } else if (next instanceof Split) {
                PropertyResolverFactory.createSplitPropertyResolver(isPartitionedStep).resolve((Split) next, properties);
            } else if (next instanceof Flow) {
                PropertyResolverFactory.createFlowPropertyResolver(isPartitionedStep).resolve((Flow) next, properties);
            }
        }

    	properties.set(PROPERTY_JOB, null);

    	return job;
    }
}

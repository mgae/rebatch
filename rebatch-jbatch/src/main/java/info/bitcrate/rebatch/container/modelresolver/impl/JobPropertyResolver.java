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

import info.bitcrate.rebatch.jaxb.JSLJob;
import info.bitcrate.rebatch.jaxb.JSLProperties;

import java.util.List;
import java.util.Properties;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;


public class JobPropertyResolver extends AbstractPropertyResolver<JSLJob> {

    /*public JobPropertyResolver(boolean isPartitionStep) {
        super(isPartitionStep);
    }*/

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

        if (job.getListeners() != null) {
           	_resolve(job.getListeners().getListenerList(), properties);
        }

       	_resolve(job.getExecutionElements(), properties);

    	properties.set(PROPERTY_JOB, null);

    	return job;
    }
    
    @Override
    public JSLJob resolve(JSLJob job, JobContext jobContext) {
    	job.setId(resolveReferences(job.getId(), jobContext));
    	job.setRestartable(resolveReferences(job.getRestartable(), jobContext));
    	
    	JSLProperties jslProperties = job.getProperties();
    	
    	if (jslProperties != null) {
    		resolveJSLProperties(jslProperties, jobContext);
    	} 

        if (job.getListeners() != null) {
           	_resolve(job.getListeners().getListenerList(), jobContext);
        }

       	_resolve(job.getExecutionElements(), jobContext);

    	return job;
    }
    
    @Override
    public JSLJob resolve(JSLJob job, StepContext stepContext) {
    	job.setId(resolveReferences(job.getId(), (StepContext) null));
    	job.setRestartable(resolveReferences(job.getRestartable(), (StepContext) null));
    	
    	JSLProperties jslProperties = job.getProperties();
    	
    	if (jslProperties != null) {
    		resolveJSLProperties(jslProperties, (StepContext) null);
    	} 

        if (job.getListeners() != null) {
           	_resolve(job.getListeners().getListenerList(), (StepContext) null);
        }

       	_resolve(job.getExecutionElements(), stepContext);

    	return job;
    }
}

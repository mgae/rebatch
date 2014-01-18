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
package info.bitcrate.rebatch.container.impl;

import info.bitcrate.rebatch.container.navigator.ModelNavigator;
import info.bitcrate.rebatch.jaxb.JSLJob;
import info.bitcrate.rebatch.jaxb.JSLProperties;
import info.bitcrate.rebatch.jaxb.Property;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.Metric;
import javax.batch.runtime.context.JobContext;


public class JobContextImpl implements JobContext {
    private BatchStatus batchStatus = null;
    private String exitStatus = null;

    private Object transientUserData = null;
    private ModelNavigator<JSLJob> navigator = null;

    private String id;  // Name
    private Properties properties = new Properties();

    private long executionId;
    private long instanceId;
    protected String restartOn;
    
    private ConcurrentHashMap<String, MetricImpl> metrics = new ConcurrentHashMap<String, MetricImpl>();

    public JobContextImpl(final ModelNavigator<JSLJob> navigator, final JSLProperties jslProperties) {
        this.navigator = navigator;
        this.id = navigator.getRootModelElement().getId();
        this.batchStatus = BatchStatus.STARTING;
        this.properties = convertJSProperties(jslProperties);
    }

    private Properties convertJSProperties(final JSLProperties jslProperties) {
        final Properties jobProperties = new Properties();
        if (jslProperties != null) { // null if not job properties defined.
            for (Property property : jslProperties.getPropertyList()) {
                jobProperties.setProperty(property.getName(), property.getValue());
            }
        }
        return jobProperties;
    }

    public MetricImpl getMetric(MetricImpl.MetricType metricType) {
        return (MetricImpl) metrics.get(metricType.name());
    }
    
    public void accumulate(MetricImpl metric) {
    	Metric.MetricType type = metric.getType();
    	String name = type.name();
    	MetricImpl m = this.metrics.get(name);
    	
    	if (m != null) {
    		m.incValueBy(metric.getValue());
    	} else {
        	metrics.put(name, new MetricImpl(type, metric.getValue()));
    	}
    }
    
    public void accumulateMetric(MetricImpl.MetricType metricType, long value) {
    	String name = metricType.name();
    	Metric m = metrics.get(name);
    	
    	long accumluated = ((m != null) ? m.getValue() : 0L) + value;
    	
    	metrics.put(name, new MetricImpl(metricType, accumluated));
    }
    
    public ModelNavigator<JSLJob> getNavigator() {
        return navigator;
    }

    public String getExitStatus() {
        return exitStatus;
    }


    public void setExitStatus(String exitStatus) {
        this.exitStatus = exitStatus;
    }

    public String getJobName() {
        return id;
    }

    public BatchStatus getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(BatchStatus batchStatus) {
        this.batchStatus = batchStatus;
    }

    public Object getTransientUserData() {
        return transientUserData;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setTransientUserData(Object data) {
        this.transientUserData = data;
    }

    @Override
    public long getExecutionId() {
        return this.executionId;
    }

    @Override
    public long getInstanceId() {
        return this.instanceId;
    }

    public void setExecutionId(long executionId) {
        this.executionId = executionId;
    }

    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    public String getRestartOn() {
        return restartOn;
    }

    public void setRestartOn(String restartOn) {
        this.restartOn = restartOn;
    }

    @Override
    public String toString() {
        return ("batchStatus = " + batchStatus) + " , exitStatus = " + exitStatus + " , id = "
            + id + " , executionId = " + executionId + " , instanceId = " + instanceId + " , restartOn = " + restartOn;
    }
}


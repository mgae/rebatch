/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.bitcrate.rebatch.jmx;

import javax.management.openmbean.TabularData;

public interface RebatchMBean {
    String DEFAULT_OBJECT_NAME = "info.bitcrate.rebatch:type=rebatch,name=operator";

    String[] getJobNames();
    int getJobInstanceCount(String jobName);
    TabularData getJobInstances(String jobName, int start, int count);
    Long[] getRunningExecutions(String jobName);
    TabularData getParameters(long executionId);
    TabularData getJobInstance(long executionId);
    TabularData getJobExecutions(long id, String name);
    TabularData getJobExecution(long executionId);
    TabularData getStepExecutions(long jobExecutionId);
    long start(String jobXMLName, final String jobParameters);
    long restart(long executionId, final String restartParameters);
    void stop(long executionId);
    void abandon(long executionId);
}

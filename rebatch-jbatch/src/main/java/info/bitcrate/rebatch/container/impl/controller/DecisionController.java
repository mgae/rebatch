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
package info.bitcrate.rebatch.container.impl.controller;

import info.bitcrate.rebatch.container.ExecutionElementController;
import info.bitcrate.rebatch.container.exception.BatchContainerRuntimeException;
import info.bitcrate.rebatch.container.impl.jobinstance.RuntimeJobExecution;
import info.bitcrate.rebatch.container.jsl.ExecutionElement;
import info.bitcrate.rebatch.container.proxy.DeciderProxy;
import info.bitcrate.rebatch.container.proxy.InjectionReferences;
import info.bitcrate.rebatch.container.proxy.ProxyFactory;
import info.bitcrate.rebatch.container.services.ServicesManager;
import info.bitcrate.rebatch.container.status.ExecutionStatus;
import info.bitcrate.rebatch.container.status.ExtendedBatchStatus;
import info.bitcrate.rebatch.jaxb.Decision;
import info.bitcrate.rebatch.jaxb.Property;
import info.bitcrate.rebatch.spi.PersistenceManagerService;

import javax.batch.runtime.StepExecution;

import java.util.List;

public class DecisionController implements ExecutionElementController {
    private RuntimeJobExecution jobExecution;

    private Decision decision;

    private StepExecution[] previousStepExecutions = null;

    private final PersistenceManagerService persistenceService;

    public DecisionController(final RuntimeJobExecution jobExecution, final Decision decision) {
        this.jobExecution = jobExecution;
        this.decision = decision;
        this.persistenceService = ServicesManager.service(PersistenceManagerService.class);
    }

    @Override
    public ExecutionStatus execute() {
        final String deciderId = decision.getRef();
        final List<Property> propList = (decision.getProperties() == null) ? null : decision.getProperties().getPropertyList();

        //Create a decider proxy and inject the associated properties

		/* Set the contexts associated with this scope */
        //job context is always in scope
        //the parent controller will only pass one valid context to a decision controller
        //so two of these contexts will always be null
        final InjectionReferences injectionRef = new InjectionReferences(jobExecution.getJobContext(), null, propList);

        final DeciderProxy deciderProxy = ProxyFactory.createDeciderProxy(deciderId, injectionRef, jobExecution);

        final String exitStatus = deciderProxy.decide(this.previousStepExecutions);

        //Set the value returned from the decider as the job context exit status.
        this.jobExecution.getJobContext().setExitStatus(exitStatus);

        return new ExecutionStatus(ExtendedBatchStatus.NORMAL_COMPLETION, exitStatus);
    }

    protected void setPreviousStepExecutions(ExecutionElement previousExecutionElement, ExecutionElementController previousElementController) {
        if (previousExecutionElement == null) {
            // only job context is available to the decider
        } else if (previousExecutionElement instanceof Decision) {

            throw new BatchContainerRuntimeException("A decision cannot precede another decision.");

        }

        List<Long> previousStepExecsIds = previousElementController.getLastRunStepExecutions();

        StepExecution[] stepExecArray = new StepExecution[previousStepExecsIds.size()];

        for (int i = 0; i < stepExecArray.length; i++) {
            StepExecution stepExec = persistenceService.getStepExecutionByStepExecutionId(previousStepExecsIds.get(i));
            stepExecArray[i] = stepExec;
        }

        this.previousStepExecutions = stepExecArray;

    }


    @Override
    public void stop() {
        // no-op
    }

    @Override
    public List<Long> getLastRunStepExecutions() {
        // TODO Auto-generated method stub
        return null;
    }


}

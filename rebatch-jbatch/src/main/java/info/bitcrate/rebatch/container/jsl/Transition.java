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
package info.bitcrate.rebatch.container.jsl;


public class Transition {
    private TransitionElement transitionElement;
    private ExecutionElement executionElement;

    private boolean finishedTransitioning = false;
    private boolean noTransitionElementMatchedAfterException = false;

    public TransitionElement getTransitionElement() {
        return transitionElement;
    }

    public ExecutionElement getNextExecutionElement() {
        return executionElement;
    }

    public void setTransitionElement(final TransitionElement transitionElement) {
        this.transitionElement = transitionElement;
    }

    public void setNextExecutionElement(final ExecutionElement executionElement) {
        this.executionElement = executionElement;
    }

    public boolean isFinishedTransitioning() {
        return finishedTransitioning;
    }

    public void setFinishedTransitioning() {
        this.finishedTransitioning = true;
    }

    public void setNoTransitionElementMatchAfterException() {
        this.noTransitionElementMatchedAfterException = true;
    }

    public boolean noTransitionElementMatchedAfterException() {
        return noTransitionElementMatchedAfterException;
    }
}

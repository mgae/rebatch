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

import info.bitcrate.rebatch.container.jsl.TransitionElement;
import info.bitcrate.rebatch.jaxb.End;
import info.bitcrate.rebatch.jaxb.Fail;
import info.bitcrate.rebatch.jaxb.Next;
import info.bitcrate.rebatch.jaxb.Stop;

import java.util.List;
import java.util.Properties;

public class ControlElementPropertyResolver extends AbstractPropertyResolver<TransitionElement> {

    public ControlElementPropertyResolver(boolean isPartitionStep) {
        super(isPartitionStep);
    }

    @Override
    public TransitionElement resolve(
    		TransitionElement controlElement,
    		List<Properties> properties) {

        controlElement.setOn(resolveReferences(controlElement.getOn(), properties));

        if (controlElement instanceof End) {
            final End end = (End) controlElement;
            end.setExitStatus(resolveReferences(end.getExitStatus(), properties));
        } else if (controlElement instanceof Fail) {
            final Fail fail = (Fail) controlElement;
            fail.setExitStatus(resolveReferences(fail.getExitStatus(), properties));
        } else if (controlElement instanceof Next) {
            final Next next = (Next) controlElement;
            next.setTo(resolveReferences(next.getTo(), properties));
        } else if (controlElement instanceof Stop) {
            final Stop stop = (Stop) controlElement;
            stop.setExitStatus(resolveReferences(stop.getExitStatus(), properties));
            stop.setRestart(resolveReferences(stop.getRestart(), properties));
        }

    	return controlElement;
    }
}

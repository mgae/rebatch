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

import info.bitcrate.rebatch.jaxb.Listener;

import java.util.List;
import java.util.Properties;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

public class ListenerPropertyResolver extends
		AbstractPropertyResolver<Listener> {

	@Override
	public Listener resolve(Listener listener, List<Properties> properties) {
		listener.setRef(resolveReferences(listener.getRef(), properties));
		resolveJSLProperties(listener.getProperties(), properties);

		return listener;
	}
	
	@Override
	public Listener resolve(Listener listener, JobContext jobContext) {
		listener.setRef(resolveReferences(listener.getRef(), jobContext));
		resolveJSLProperties(listener.getProperties(), jobContext);

		return listener;
	}
	
	@Override
	public Listener resolve(Listener listener, StepContext stepContext) {
		listener.setRef(resolveReferences(listener.getRef(), stepContext));
		resolveJSLProperties(listener.getProperties(), stepContext);

		return listener;
	}
}

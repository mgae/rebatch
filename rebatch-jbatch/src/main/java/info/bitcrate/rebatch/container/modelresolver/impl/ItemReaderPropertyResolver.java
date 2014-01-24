/**
 * Copyright 2013 International Business Machines Corp.
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

import info.bitcrate.rebatch.jaxb.ItemReader;

import java.util.List;
import java.util.Properties;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

public class ItemReaderPropertyResolver extends
		AbstractPropertyResolver<ItemReader> {

	@Override
	public ItemReader resolve(ItemReader reader, List<Properties> properties) {
		reader.setRef(resolveReferences(reader.getRef(), properties));
		resolveJSLProperties(reader.getProperties(), properties);

		return reader;
	}

	@Override
	public ItemReader resolve(ItemReader reader, JobContext jobContext) {
		reader.setRef(resolveReferences(reader.getRef(), jobContext));
		resolveJSLProperties(reader.getProperties(), jobContext);

		return reader;
	}

	@Override
	public ItemReader resolve(ItemReader reader, StepContext stepContext) {
		reader.setRef(resolveReferences(reader.getRef(), stepContext));
		resolveJSLProperties(reader.getProperties(), stepContext);

		return reader;
	}
}

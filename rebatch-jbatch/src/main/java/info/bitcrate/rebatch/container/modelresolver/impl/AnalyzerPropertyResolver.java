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

import info.bitcrate.rebatch.jaxb.Analyzer;

import java.util.List;
import java.util.Properties;

public class AnalyzerPropertyResolver extends
		AbstractPropertyResolver<Analyzer> {

	public AnalyzerPropertyResolver(boolean isPartitionStep) {
		super(isPartitionStep);
	}

	@Override
	public Analyzer resolve(Analyzer analyzer, List<Properties> properties) {

		analyzer.setRef(resolveReferences(analyzer.getRef(), properties));

		if (analyzer.getProperties() != null) {
			resolveJSLProperties(analyzer.getProperties(), properties);
		}

		return analyzer;
	}
}

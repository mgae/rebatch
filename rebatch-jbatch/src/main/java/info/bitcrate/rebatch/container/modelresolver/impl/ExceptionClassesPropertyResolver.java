/**
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

import info.bitcrate.rebatch.jaxb.ExceptionClassFilter;

import java.util.List;
import java.util.Properties;


public class ExceptionClassesPropertyResolver extends AbstractPropertyResolver<ExceptionClassFilter> {

    public ExceptionClassesPropertyResolver(boolean isPartitionStep) {
        super(isPartitionStep);
    }

    @Override
    public ExceptionClassFilter resolve(
    		ExceptionClassFilter exceptionClassFilter,
    		List<Properties> properties) {
    
        // Resolve ExceptionClassFilter properties
        if (exceptionClassFilter.getIncludeList() != null) {
            for (final ExceptionClassFilter.Include includeElem : exceptionClassFilter.getIncludeList()) {
                includeElem.setClazz(resolveReferences(includeElem.getClazz(), properties));
            }

            for (final ExceptionClassFilter.Exclude excludeElem : exceptionClassFilter.getExcludeList()) {
                excludeElem.setClazz(resolveReferences(excludeElem.getClazz(), properties));
            }
        }

        return exceptionClassFilter;
    }
}

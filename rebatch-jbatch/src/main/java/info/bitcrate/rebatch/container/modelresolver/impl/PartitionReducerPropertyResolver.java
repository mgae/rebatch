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

import info.bitcrate.rebatch.jaxb.PartitionReducer;

import java.util.List;
import java.util.Properties;


public class PartitionReducerPropertyResolver extends
    AbstractPropertyResolver<PartitionReducer> {

    public PartitionReducerPropertyResolver(boolean isPartitionStep) {
        super(isPartitionStep);
    }

    @Override
    public PartitionReducer resolve(
    		PartitionReducer reducer,
    		List<Properties> properties) {

        reducer.setRef(resolveReferences(reducer.getRef(), properties));

        // Resolve all the properties defined for this artifact
        if (reducer.getProperties() != null) {
        	resolveJSLProperties(reducer.getProperties(), properties);
        }

    	return reducer;
    }
}

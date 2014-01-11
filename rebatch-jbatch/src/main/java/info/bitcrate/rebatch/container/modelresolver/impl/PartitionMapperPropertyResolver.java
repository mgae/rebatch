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

import info.bitcrate.rebatch.jaxb.PartitionMapper;

import java.util.Properties;


public class PartitionMapperPropertyResolver extends
    AbstractPropertyResolver<PartitionMapper> {

    public PartitionMapperPropertyResolver(boolean isPartitionStep) {
        super(isPartitionStep);
    }

    @Override
    public PartitionMapper substituteProperties(PartitionMapper partitionMapper,
                                                Properties submittedProps, Properties parentProps) {

		/*
		<xs:complexType name="PartitionMapper">
			<xs:sequence>
				<xs:element name="properties" type="jsl:Properties" minOccurs="0" maxOccurs="1" />
			</xs:sequence>
			<xs:attribute name="ref" use="required" type="jsl:artifactRef" />
		</xs:complexType>
		*/

        partitionMapper.setRef(
        		this.replaceAllProperties(
        				partitionMapper.getRef(), 
        				submittedProps, 
        				parentProps));

        // Resolve all the properties defined for this step
        if (partitionMapper.getProperties() != null) {
            resolveElementProperties(
            		partitionMapper.getProperties().getPropertyList(), 
            		submittedProps, 
            		parentProps);
        }

        return partitionMapper;
    }

}

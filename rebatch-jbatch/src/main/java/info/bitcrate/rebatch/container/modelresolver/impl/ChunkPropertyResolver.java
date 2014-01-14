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

import info.bitcrate.rebatch.container.modelresolver.PropertyResolverFactory;
import info.bitcrate.rebatch.jaxb.Chunk;

import java.util.List;
import java.util.Properties;

public class ChunkPropertyResolver extends AbstractPropertyResolver<Chunk> {

    public ChunkPropertyResolver(boolean isPartitionStep) {
        super(isPartitionStep);
    }

    @Override
    public Chunk resolve(Chunk chunk, List<Properties> properties) {

        chunk.setCheckpointPolicy(resolveReferences(chunk.getCheckpointPolicy(), properties));
        chunk.setItemCount(resolveReferences(chunk.getItemCount(), properties));
        chunk.setTimeLimit(resolveReferences(chunk.getTimeLimit(), properties));
        chunk.setSkipLimit(resolveReferences(chunk.getSkipLimit(), properties));
        chunk.setRetryLimit(resolveReferences(chunk.getRetryLimit(), properties));

        // Resolve Reader properties
        if (chunk.getReader() != null) {
            PropertyResolverFactory.createReaderPropertyResolver(isPartitionedStep).resolve(chunk.getReader(), properties);
        }

        // Resolve Processor properties
        if (chunk.getProcessor() != null) {
            PropertyResolverFactory.createProcessorPropertyResolver(isPartitionedStep).resolve(chunk.getProcessor(), properties);
        }

        // Resolve Writer properties
        if (chunk.getWriter() != null) {
            PropertyResolverFactory.createWriterPropertyResolver(isPartitionedStep).resolve(chunk.getWriter(), properties);
        }

        // Resolve CheckpointAlgorithm properties
        if (chunk.getCheckpointAlgorithm() != null) {
            PropertyResolverFactory.createCheckpointAlgorithmPropertyResolver(isPartitionedStep).resolve(chunk.getCheckpointAlgorithm(), properties);
        }

        // Resolve SkippableExceptionClasses properties
        if (chunk.getSkippableExceptionClasses() != null) {
            PropertyResolverFactory.createSkippableExceptionClassesPropertyResolver(isPartitionedStep).resolve(chunk.getSkippableExceptionClasses(), properties);
        }

        // Resolve RetryableExceptionClasses properties
        if (chunk.getRetryableExceptionClasses() != null) {
            PropertyResolverFactory.createRetryableExceptionClassesPropertyResolver(isPartitionedStep).resolve(chunk.getRetryableExceptionClasses(), properties);
        }

        // Resolve NoRollbackExceptionClasses properties
        if (chunk.getNoRollbackExceptionClasses() != null) {
            PropertyResolverFactory.createNoRollbackExceptionClassesPropertyResolver(isPartitionedStep).resolve(chunk.getNoRollbackExceptionClasses(), properties);
        }

    	return chunk;
    }
}

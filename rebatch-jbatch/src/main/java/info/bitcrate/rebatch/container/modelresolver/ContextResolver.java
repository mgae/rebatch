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
package info.bitcrate.rebatch.container.modelresolver;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

public interface ContextResolver<B> {

	/**
	 * Replace references to job context variables, ${jobInstanceId},
	 * ${jobExecutionId}, and ${jobName}.
	 * 
	 * @param element
	 * 			root element of the job tree to be resolved
	 * @param jobContext
	 * 			job context object to be used for reference resolution
	 * @return
	 * 			the root element
	 */
	B resolve(B element, JobContext jobContext);
	
	/**
	 * Replace references to step context variables, ${stepName} and
	 * ${stepExecutionId}.
	 * 
	 * @param element
	 * 			root element of the step tree to be resolved
	 * @param stepContext
	 * 			step context object to be used for reference resolution
	 * @return
	 * 			the root element
	 */
	B resolve(B element, StepContext stepContext);

}

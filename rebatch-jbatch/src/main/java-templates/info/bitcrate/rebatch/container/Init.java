/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.bitcrate.rebatch.container;

import info.bitcrate.rebatch.container.services.ServicesManager;

import java.util.logging.Logger;

public final class Init {
	private final static Logger LOGGER = Logger.getLogger(Init.class.getName());

	private final static String LOGO = 
			"\n"
			+ "    */*/*/              */                    */                */    \n"
			+ "   */    */    */*/    */*/*/      */*/*/  */*/*/*/    */*/*/  */*/*/ \n"
			+ "  */*/*/    */*/*/*/  */    */  */    */    */      */        */    */\n"
			+ " */    */  */        */    */  */    */    */      */        */    */ \n"
			+ "*/    */    */*/*/  */*/*/      */*/*/      */*/    */*/*/  */    */   "
			+ "${project.version}\n";

	public static void doInit() {
		if (Boolean.parseBoolean(ServicesManager.value(
				"info.bitcrate.rebatch.init.verbose", "true"))) {
			if (!Boolean.parseBoolean(ServicesManager.value(
					"info.bitcrate.rebatch.init.verbose.sysout", "false"))) {
				LOGGER.info(LOGO);
			} else {
				System.out.println(LOGO);
			}
		}
	}

	private Init() {
		// no-op
	}
}

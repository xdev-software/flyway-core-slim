/*
 * Copyright © 2024 XDEV Software (https://xdev.software)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package software.xdev.flyway;

import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;


@SuppressWarnings("java:S2160") // Nothing else to compare
public class SimpleMariaDBContainer extends MariaDBContainer<SimpleMariaDBContainer>
{
	public static final int MAJOR_VERSION = 11;
	
	public SimpleMariaDBContainer()
	{
		super(DockerImageName.parse("mariadb:" + MAJOR_VERSION));
		
		// (31.03.2022 AB)NOTE: https://github.com/testcontainers/testcontainers-java/issues/914
		// Do not mount the volume!
		this.withConfigurationOverride(null);
		
		// DO NOT resolve client hostnames for more performance
		// https://mariadb.com/docs/server/ref/mdb/system-variables/skip_name_resolve/
		this.setCommand("--skip-name-resolve");
	}
}

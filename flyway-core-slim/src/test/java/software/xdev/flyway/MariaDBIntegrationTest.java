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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.Test;


class MariaDBIntegrationTest
{
	@Test
	void check()
	{
		try(final SimpleMariaDBContainer mariaDbCnt = new SimpleMariaDBContainer())
		{
			mariaDbCnt.start();
			
			final FluentConfiguration config = Flyway.configure()
				.validateOnMigrate(false)
				.dataSource(mariaDbCnt.getJdbcUrl(), mariaDbCnt.getUsername(), mariaDbCnt.getPassword())
				.locations("flyway/mariadb");
			
			final Flyway flyway = config.load();
			final MigrateResult result = flyway.migrate();
			
			assertEquals(1, result.migrationsExecuted);
		}
	}
}

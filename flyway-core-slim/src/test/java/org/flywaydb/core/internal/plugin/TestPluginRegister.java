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
package org.flywaydb.core.internal.plugin;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flywaydb.core.api.migration.baseline.BaselineAppliedMigration;
import org.flywaydb.core.api.migration.baseline.BaselineMigrationConfigurationExtension;
import org.flywaydb.core.api.migration.baseline.BaselineMigrationResolver;
import org.flywaydb.core.api.migration.baseline.BaselineMigrationTypeResolver;
import org.flywaydb.core.api.migration.baseline.BaselineResourceTypeProvider;
import org.flywaydb.core.experimental.migration.CoreMigrationTypeResolver;
import org.flywaydb.core.extensibility.Plugin;
import org.flywaydb.core.internal.configuration.resolvers.EnvironmentProvisionerNone;
import org.flywaydb.core.internal.configuration.resolvers.EnvironmentVariableResolver;
import org.flywaydb.core.internal.resource.CoreResourceTypeProvider;
import org.flywaydb.core.internal.schemahistory.BaseAppliedMigration;
import org.flywaydb.database.mysql.MySQLDatabaseType;
import org.flywaydb.database.mysql.mariadb.MariaDBDatabaseType;
import org.junit.jupiter.api.Test;


class TestPluginRegister
{
	@Test
	void onlyExpectedPlugins()
	{
		final Map<? extends Class<? extends Plugin>, List<Plugin>> pluginTypes =
			assertDoesNotThrow(() -> new PluginRegister().getPlugins())
				.stream()
				.collect(Collectors.groupingBy(Plugin::getClass));
		
		final List<? extends Class<? extends Plugin>> expectedPluginClasses = List.of(
			BaseAppliedMigration.class,
			CoreResourceTypeProvider.class,
			EnvironmentVariableResolver.class,
			BaselineAppliedMigration.class,
			BaselineMigrationConfigurationExtension.class,
			BaselineMigrationResolver.class,
			BaselineResourceTypeProvider.class,
			BaselineMigrationTypeResolver.class,
			CoreMigrationTypeResolver.class,
			EnvironmentProvisionerNone.class,
			MySQLDatabaseType.class,
			MariaDBDatabaseType.class);
		
		assertAll(
			expectedPluginClasses.stream()
				.map(clazz ->
					() -> assertEquals(
						1,
						Optional.ofNullable(pluginTypes.remove(clazz)).orElseGet(List::of).size(),
						() -> "Plugin not found: " + clazz)));
		
		assertEquals(0, pluginTypes.size(), () -> "Found unknown plugins:" + pluginTypes.keySet());
	}
}

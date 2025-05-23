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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.extensibility.Plugin;


/**
 * Fork with the following changes:
 * <ul>
 * <li>Does not load some unused plugins/extensions</li>
 * </ul>
 */
@SuppressWarnings("unused")
public class PluginRegister
{
	// See META-INF/services of flyway-core for more information
	protected static final Set<String> REFUSE_LOAD = new HashSet<>(Set.of(
		// Prepare is only available in enterprise (and deploy uses scripts from prepare)
		"org.flywaydb.core.internal.configuration.extensions.PrepareScriptFilenameConfigurationExtension",
		"org.flywaydb.core.internal.configuration.extensions.DeployScriptFilenameConfigurationExtension",
		// Publishing
		"org.flywaydb.core.internal.publishing",
		// PROPRIETARY
		"org.flywaydb.core.internal.proprietaryStubs",
		// Integrated databases
		"org.flywaydb.core.internal.database",
		// CLEAN COMMAND is extremely dangerous and can destroy the complete database
		"org.flywaydb.core.internal.command.clean"
	));
	
	protected final List<Plugin> registeredPlugins = new ArrayList<>();
	protected final ClassLoader classLoader = this.getClass().getClassLoader();
	protected boolean hasRegisteredPlugins;
	
	public <T extends Plugin> T getPlugin(final Class<T> clazz)
	{
		return this.getPlugins()
			.stream()
			.filter(p -> p.getClass().getCanonicalName().equals(clazz.getCanonicalName()))
			.findFirst()
			.map(clazz::cast)
			.orElse(null);
	}
	
	public <T extends Plugin> List<T> getPlugins(final Class<T> clazz)
	{
		return this.getPlugins()
			.stream()
			.filter(clazz::isInstance)
			.map(clazz::cast)
			.sorted()
			.toList();
	}
	
	public <T extends Plugin> List<T> getLicensedPlugins(final Class<T> clazz, final Configuration configuration)
	{
		return this.getPlugins()
			.stream()
			.filter(clazz::isInstance)
			.filter(p -> p.isLicensed(configuration))
			.map(clazz::cast)
			.sorted()
			.toList();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Plugin> T getLicensedPlugin(final String className, final Configuration configuration)
	{
		return (T)this.getPlugins()
			.stream()
			.filter(p -> p.isLicensed(configuration))
			.filter(p -> p.getClass().getSimpleName().equals(className))
			.sorted()
			.findFirst()
			.orElse(null);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Plugin> T getPlugin(final String className)
	{
		return (T)this.getPlugins()
			.stream()
			.filter(p -> p.getClass().getSimpleName().equals(className))
			.sorted()
			.findFirst()
			.orElse(null);
	}
	
	protected List<Plugin> getPlugins()
	{
		this.registerPlugins();
		return Collections.unmodifiableList(this.registeredPlugins);
	}
	
	// This is not used on virtual threads
	@SuppressWarnings("PMD.AvoidSynchronizedStatement")
	void registerPlugins()
	{
		// Quick exit
		if(this.hasRegisteredPlugins)
		{
			return;
		}
		synchronized(this.registeredPlugins)
		{
			if(this.hasRegisteredPlugins)
			{
				return;
			}
			
			// Note: ServiceLoader still searches for the constructor and if the class has invalid imports it crashes
			ServiceLoader.load(Plugin.class, this.classLoader).stream()
				.filter(p -> REFUSE_LOAD.stream().noneMatch(refuse -> p.type().getName().startsWith(refuse)))
				.map(ServiceLoader.Provider::get)
				.filter(Plugin::isEnabled)
				.forEach(this.registeredPlugins::add);
			
			this.hasRegisteredPlugins = true;
		}
	}
	
	public PluginRegister getCopy()
	{
		final PluginRegister copy = new PluginRegister();
		copy.registeredPlugins.clear();
		copy.registeredPlugins.addAll(this.getPlugins().stream().map(Plugin::copy).toList());
		copy.hasRegisteredPlugins = true;
		return copy;
	}
}

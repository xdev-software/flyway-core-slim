# 1.0.1
Do not include MariaDB JDBC adapter in ``compile`` scope.

# 1.0.0
_Compatible with Flyway 1.20.1_

_Initial release_

Currently applied:
* Remove Jackson Databind from ``ConfigurationExtension`` so that it's no longer required and can be excluded
* ``PluginRegister`` reconfigured so that it doesn't load the following plugins/extensions:
  * Integrated databases (H2, SQLite, Testcontainers-Adapter)
  * Clean Command (excluded because it can destory the complete database by accident if called in production)
  * Extensions that are only avialable for Enterprise (deploy/prepare)
  * Publishing subsystem
  * All proprietary plugins

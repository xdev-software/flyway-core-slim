# 1.1.0
_Compatible with Flyway 11.7.2_

* Compatible with Flyway 11.7.2 (shipped with Spring Boot 3.5) #40

# 1.0.2
* Migrated deployment to _Sonatype Maven Central Portal_ [#155](https://github.com/xdev-software/standard-maven-template/issues/155)
* Updated dependencies

# 1.0.1
Do not include MariaDB JDBC adapter in ``compile`` scope.

# 1.0.0
_Compatible with Flyway 10.20.1_

_Initial release_

Currently applied:
* Remove Jackson Databind from ``ConfigurationExtension`` so that it's no longer required and can be excluded
* ``PluginRegister`` reconfigured so that it doesn't load the following plugins/extensions:
  * Integrated databases (H2, SQLite, Testcontainers-Adapter)
  * Clean Command (excluded because it can destory the complete database by accident if called in production)
  * Extensions that are only avialable for Enterprise (deploy/prepare)
  * Publishing subsystem
  * All proprietary plugins

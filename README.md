[![Latest version](https://img.shields.io/maven-central/v/software.xdev/flyway-core-slim?logo=apache%20maven)](https://mvnrepository.com/artifact/software.xdev/flyway-core-slim)
[![Build](https://img.shields.io/github/actions/workflow/status/xdev-software/flyway-core-slim/check-build.yml?branch=develop)](https://github.com/xdev-software/flyway-core-slim/actions/workflows/check-build.yml?query=branch%3Adevelop)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=xdev-software_flyway-core-slim&metric=alert_status)](https://sonarcloud.io/dashboard?id=xdev-software_flyway-core-slim)

# flyway-core-slim

Overrides [``flyway-core``](https://github.com/flyway/flyway) in a way that [ unused things](https://github.com/flyway/flyway/issues/3893) are excluded.

Details about exclusions are available in the [changelogs](./CHANGELOG.md).

## Usage

This library is primarily designed to be used in conjunction with Spring Boot projects.

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>...</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>software.xdev</groupId>
        <artifactId>flyway-core-slim</artifactId>
        <!-- You should ensure that this version is compatible with the flyway version provided by Spring Boot -->
        <version>...</version>
    </dependency>
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-mysql</artifactId>
    </dependency>
</dependencies>
```

## Installation
[Installation guide for the latest release](https://github.com/xdev-software/flyway-core-slim/releases/latest#Installation)

## Support
If you need support as soon as possible and you can't wait for any pull request, feel free to use [our support](https://xdev.software/en/services/support).

## Contributing
See the [contributing guide](./CONTRIBUTING.md) for detailed instructions on how to get started with our project.

## Dependencies and Licenses
View the [license of the current project](LICENSE) or the [summary including all dependencies](https://xdev-software.github.io/flyway-core-slim/dependencies)

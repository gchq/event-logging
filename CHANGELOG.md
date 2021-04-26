# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/) 
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]

## [v4.0.9_schema-v3.2.4] - 2021-04-26

* Change filename of Maven artifacts from `event-logging-api...` to `event-logging...`.


## [v4.0.8_schema-v3.2.4] - 2021-04-26

* Revert to java 8


## [v4.0.7_schema-v3.2.4] - 2018-09-07

* Update to Java 10

* Issue **6** : Fix hard coded success value in EventLoggingUtil.createOutcome().


## [v4.0.5_schema-v3.2.4] - 2018-09-05

* Added automatic module name for Java 9 builds.


## [v3.2.4_schema-v3.2.4] - 2018-05-30

* Fix for log receiver class name.


## [v3.2.3_schema-v3.2.4] - 2018-05-22


## [v3.2.1_schema-v3.2.4] - 2018-02-15

### Changed

* Uplifted schema to v3.2.4

* Add _-client_ to the end of the `id` attribute value on the `schema` element.


## [v3.2.0_schema-v3.2.0] - 2017-12-21

### Added

* Issue **gchq/event-logging-schema#23** : Add optional `Coordinates` object to the `Location` class to capture latitude/longitude

### Changed

* Uplifted schema to v3.2.0


## [v3.1.2_schema-v3.1.2] - 2017-11-28

### Added

* Add Bintray deployment


## [v3.1.1_schema-v3.1.2] - 2017-11-14

### Added

* Add sources and javadoc jars

### Changed

* Uplift schema to v3.1.2

* Change build to Gradle


## [v3.1.0] - 2017-04-05
Intial open source release

[Unreleased]: https://github.com/gchq/event-logging/compare/v4.0.9_schema-v3.2.4...HEAD
[v4.0.9_schema-v3.2.4]: https://github.com/gchq/event-logging/compare/v4.0.8_schema-v3.2.4...v4.0.9_schema-v3.2.4
[v4.0.8_schema-v3.2.4]: https://github.com/gchq/event-logging/compare/v4.0.7_schema-v3.2.4...v4.0.8_schema-v3.2.4
[v4.0.7_schema-v3.2.4]: https://github.com/gchq/event-logging/compare/v4.0.5_schema-v3.2.4...v4.0.7_schema-v3.2.4
[v4.0.5_schema-v3.2.4]: https://github.com/gchq/event-logging/compare/v3.2.4_schema-v3.2.4...v4.0.5_schema-v3.2.4
[v3.2.4_schema-v3.2.4]: https://github.com/gchq/event-logging/compare/v3.2.3_schema-v3.2.4...v3.2.4_schema-v3.2.4
[v3.2.3_schema-v3.2.4]: https://github.com/gchq/event-logging/compare/v3.2.1_schema-v3.2.4...v3.2.3_schema-v3.2.4
[v3.2.1_schema-v3.2.4]: https://github.com/gchq/event-logging/compare/v3.2.0_schema-v3.2.0...v3.2.1_schema-v3.2.4
[v3.2.0_schema-v3.2.0]: https://github.com/gchq/event-logging/compare/v3.1.2_schema-v3.1.2...v3.2.0_schema-v3.2.0
[v3.1.2_schema-v3.1.2]: https://github.com/gchq/event-logging/compare/v3.1.1_schema-v3.1.2...v3.1.2_schema-v3.1.2
[v3.1.1_schema-v3.1.2]: https://github.com/gchq/event-logging/compare/v3.1.0...v3.1.1_schema-v3.1.2

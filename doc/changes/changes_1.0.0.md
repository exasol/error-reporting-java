# error-reporting-java 1.0.0, released 2022-08-30

Code name: Quoting enhancements

In this release we added a guideline for deleting error codes and migrated the project to Project Keeper 2.

When you use Java types `Path`, `File`, `URL` or `URI` as parameter in a message, it now automatically gets quoted. Note that this can break existing unit tests in you code or client code that parses error messages.

We also now support quoting with double quotes.

Note that we removed the deprecated `unquotedParameter` methods.

Quoting is now exclusively controlled by the following single-character switches:

`u`
: unquoted

`q`
: forced single quotes

`d`
: forced double quotes

none
: automatic quoting depending on the type

If multiple conflicting switches are given, the one with the highest precedence (see list above) is taken.
That means the previous `uq` switch still works because the `q` is ignored in this case.

For the reviewers: open TODOs for this release: #28, #27, #19

## Documentation

* #30: Improved documentation

## Dependency Updates

### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter-engine:5.8.1` to `5.9.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.8.1` to `5.9.0`

### Plugin Dependency Updates

* Added `com.exasol:error-code-crawler-maven-plugin:1.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:1.3.2` to `2.6.2`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.14` to `0.15`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.1.0` to `2.5`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.8.1` to `3.10.1`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:2.8.2` to `3.0.0-M1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:2.5.2` to `2.4`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.2.0` to `2.4`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.3.1` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.2.0` to `2.6`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.9.1` to `3.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M3` to `3.0.0-M5`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.2.7`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.8.1` to `2.10.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.7` to `0.8.8`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
* Updated `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.1.0` to `3.2.0`
* Updated `org.sonatype.plugins:nexus-staging-maven-plugin:1.6.8` to `1.6.13`

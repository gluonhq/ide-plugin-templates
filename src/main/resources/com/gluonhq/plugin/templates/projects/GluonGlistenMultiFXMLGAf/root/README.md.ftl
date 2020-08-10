# Gluon Mobile - Multi View

Gluon Mobile Applications are Java application written in JavaFX.
These applications ensure that developers can create high performance, great looking, and cloud connected mobile apps from a single Java code base.

This sample contains a simple Gluon Mobile application which can be used to run, test and create a native application for any targeted device.
The sample leverages Glisten Afterburner framework, built on top of [afterburner.fx](http://afterburner.adam-bien.com/), to inject
models and services into Presenter.

## Pre-requisites

Please checkout the prerequisites to run this application [here](https://github.com/gluonhq/client-maven-plugin#requirements).

## Instructions

> **Note**: The following are command line instructions. For IDE specific instructions please checkout
[IDE documentation](https://docs.gluonhq.com/client/#_ide) of the client plugin.

These application can run on the JVM on desktop platforms. To run the application, execute the following command:

```
<#if buildTool == "maven">
mvn javafx:run
<#elseif buildTool == "gradle">
./gradlew run
</#if>
```

The same application can also run natively for on any targeted OS, including Android, iOS, Linux, Mac and Windows.

To create a native image, execute the following command:

```
<#if buildTool == "maven">
mvn client:build client:run
<#elseif buildTool == "gradle">
./gradlew nativeBuild nativeRun
</#if>
```

> **Note**: The above client commands are target-platform dependent and might change depending on the platform.
For more details, please check
<#if buildTool == "maven">
    [Client Maven Goals](https://github.com/gluonhq/client-maven-plugin#2-goals).
<#elseif buildTool == "gradle">
    [Client Gradle Tasks](https://github.com/gluonhq/client-gradle-plugin/#2-tasks)
</#if>

## Configuration

To configure the client plugin, please checkout the [Configuration documentation](https://docs.gluonhq.com/client/#_configuration).

## More information

Here are some helpful links:

* [Gluon Client documentation](https://docs.gluonhq.com/client)
* [Gluon Mobile documentation](https://docs.gluonhq.com/mobile)
* [Client Maven Plugin](https://github.com/gluonhq/client-maven-plugin)
* [Client Gradle Plugin](https://github.com/gluonhq/client-gradle-plugin)
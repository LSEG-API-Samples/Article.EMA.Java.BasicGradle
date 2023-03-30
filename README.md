# Set Up Enterprise Message API Java Application with Gradle
# Set Up Refinitiv Real-Time SDK Java Application with Maven
- version: draft
- Last update: Apr 2023
- Environment: Windows, Linux
- Compiler: Java
- Prerequisite: [Demo prerequisite](#prerequisite)

## <a id="Introduction"></a>Introduction


[Refinitiv Real-Time SDK (Java Edition)](https://developers.refinitiv.com/en/api-catalog/refinitiv-real-time-opnsrc/rt-sdk-java) (RTSDK, formerly known as Elektron SDK) is a suite of modern and open source APIs that aim to simplify development through a strong focus on ease of use and standardized access to a broad set of Refinitiv proprietary content and services via the proprietary TCP connection named RSSL and proprietary binary message encoding format named OMM Message. The capabilities range from low latency/high-performance APIs right through to simple streaming Web APIs. 

The SDK has been released on the [Maven Central Repository](https://central.sonatype.com/) to support the modern Java development life cycle since the RTSDK Java (formerly known as Elektron SDK) version 1.2. The Maven Central Repository supported also lets SDK compatibilities with the Java build automation tools like [Gradle](https://gradle.org/) and [Apache Maven](https://maven.apache.org/). This helps Java developers to build RTSDK Java applications, manage its dependencies (Java Developers do not need to manually manage different versions of jar files anymore), and better collaboration in the team.

The RTSDK Java package comes with Gradle build tool supported by default. However, the included Gradle configuration files are very complex for supporting the SDK multiple APIs (EMA and ETA), and use cases (Core API code, examples, ValueAdd package, etc). If developers want to use Gradle, the RTSDK Java's Gradle setings might not be a good starting point for them.

That is why I am creating this project to show how to use a simple Gradle configuration to work with the EMA Java API. Existing developers who are currently using Gradle also understand how to integrate RTSDK Java into their Gradle project.

Note: 
- This article is based on EMA Java version 3.7.0 L1 (RTSDK Java Edition 2.1.0 L1) and Gradle version 7.3.3 (using Grooy DSL)

## <a id="why_gradle"></a>Why you need build automation tool

The modern Java build automation tools help developers to automate the software build and project management processes including compiling, dependency manager, packing, and running tests. 

If you want to manage the EMA Java application project manually (the old way), you need to manage total **26 jar files** (*as of April 2023*) required by the API as follows:

![Alt text](images/01_jars_files.png)

And developers need to add all jar files to the classpath when running the EMA Java application manually like the following example 

``` bash
$>java -cp .;%EMAJ_HOME%\Libs\ema-<version>.jar;%ETAJ_HOME%\Libs\eta-<version>.jar;%ETAJ_HOME%\Libs\etaValueAdd-<version>.jar;%ETAJ_HOME%\Libs\etaValueAddCache-<version>.jar;%ETAJ_HOME%\Libs\etajConverter-<version>.jar;%ETAJ_HOME%\Libs\ansipage-<version>.jar;%BINPAK%\Ema\Libs\apache\commons-configuration2-2.8.0.jar;%BINPAK%\Ema\Libs\apache\commons-lang3-3.9.jar;%BINPAK%\Ema\Libs\apache\commons-logging-1.2.jar;%BINPAK%\Ema\Libs\apache\commons-text-1.9.jar;%BINPAK%\Ema\Libs\xpp3-1.1.4c.jar;%BINPAK%\Eta\Libs\jackson-annotations-2.14.1.jar;%BINPAK%\Eta\Libs\jackson-core-2.14.1.jar;%BINPAK%\Eta\Libs\jackson-databind-2.14.1.jar;%BINPAK%\Eta\Libs\json-20210307.jar;%BINPAK%\Eta\Libs\lz4-java-1.8.0.jar;%BINPAK%\Eta\Libs\ApacheClient\commons-codec-1.11.jar;%BINPAK%\Eta\Libs\ApacheClient\httpclient-4.5.13.jar;%BINPAK%\Eta\Libs\ApacheClient\httpclient-cache-4.5.13.jar;%BINPAK%\Eta\Libs\ApacheClient\httpcore-4.4.13.jar;%BINPAK%\Eta\Libs\ApacheClient\httpcore-nio-4.4.13.jar;%BINPAK%\Eta\Libs\ApacheClient\httpmime-4.5.13.jar;%BINPAK%\Eta\Libs\jose4j\jose4j-0.9.1.jar;%BINPAK%\Eta\Libs\SLF4J\slf4j-1.7.32\slf4j-api-1.7.32.jar;%BINPAK%\Eta\Libs\SLF4J\slf4j-1.7.32\slf4j-jdk14-1.7.32.jar com.refinitiv.ema.examples.training.consumer.series100.ex100_MP_Streaming.Consmer 
```

The example above is just for running the EMA Java application, the real development project needs to connect to more services,  which means the project needs more jar files, configuration files, etc to manage. This makes the development project hard to set up and hard to collaborate among peers.

The build automation tool can help simplify all of this complexity by helps team manage the project dependencies, standardize project structure with a simple configuration setting as follows:

``` Grovy
// tag::dependencies[]
dependencies {
    // This dependency is used by the application.
    implementation 'com.refinitiv.ema:ema:3.7.0.0'
}
```
And it lets developers compile, run, and test the application easier command.

``` Bash
$> gradlew run
```
## Prerequisite

Before I am going further, there is some prerequisite, dependencies software and libraries that the project is needed.


### Java SDK

Firstly, you need Java SDK. Please check for the supported Java version from the [API Compatibility Matrix](https://developers.refinitiv.com/en/api-catalog/refinitiv-real-time-opnsrc/rt-sdk-java/documentation#api-compatibility-matrix) page. 

I am using the Open JDK version 11 in this project (as of April 2023).

### Gradle

Next, the [Gradle](https://gradle.org/) build automation tool. Please follow [Gradle installation guide document](https://gradle.org/install/).

### Gradle Wrapper version 7.3.3

Basically, developers use the [Gradle Wrapper](https://docs.gradle.org/7.3.3/userguide/gradle_wrapper.html) to interact with Gradle build. The Wrapper (```gradlew``` and ```gradlew.bat```) is a script that invokes a declared version of Gradle, downloading it beforehand if necessary. 

This project uses the Gradle Wrapper version 7.3.3 (and above). You can check the current Gradle Wrapper version from the following command:

``` Bash
$> gradlew --version
```

If your Gradle Wrapper is older than version 7.3.3, you can update the Wrapper with the following command:

```  Bash
$> gradlew wrapper --gradle-version=7.3.3
```
### Access to the Refinitiv Real-Time Optimized

This project uses [Customer Identity and Access Management (CIAM) ](https://developers.refinitiv.com/en/article-catalog/article/changes-to-customer-access-and-identity-management--refinitiv-re) (aka Version 2 Authentication) - Client Credentials Grant Model to connect to the Refinitiv Real-Time Optimized (RTO).

Please contact your Refinitiv's representative to help you with the RTO account and services.

Note: This is for the *CloudConsumer* example only.

### Access to Refinitiv Real-Time Distribution System

Note: This is for the *LocalConsumer* example only. Please contact your Market Data team to help you with the Refinitiv Real-Time Distribution System (RTDS).

## <a id="what_gradle"></a>What is Gradle?

[Gradle](https://gradle.org/) is the multi-language open-source build automation tool. It helps developers and the team to organize the project structure, manage dependencies, process development tasks such as running the application, packaing/publishing, testing, and much more. The tool is designed for multi-project builds, aims for high performance, supports multiple planforms (runs on the JVM), supports custom tasks and plugins, and fully IDE supported.

Gradle is the official build tool for Android development platform. 

## <a id="gradle_layout"></a>Project Sturcture 

Gradle is designed for multi-projects develpment. A recommend structure consists of one root project, and one or more subprojects (the [RTSDK Java](https://github.com/Refinitiv/Real-Time-SDK/tree/master/Java) uses the same structure too). The folder diagram is as follow:

```
.
├── subproject
│   ...
│   ├── build.gradle
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── ...
│       │   └── resources
│       │       └── ...
│       └── test
│       	   └── resources
│       	      └── ...
└── settings.gradle
```
- *&lt;root&gt;/settings.gradle*: Defined the subprojects to include. 
- *&lt;root&gt;/&lt;subproject&gt;/build.gradle*: Subproject configruration
- *&lt;root&gt;/&lt;subproject&gt;/src/main/java*: Application/Library sources
- *&lt;root&gt;/&lt;subproject&gt;/src/main/resources*:	Application/Library resources
- *&lt;root&gt;/&lt;subproject&gt;/src/test/java*:	Test sources
- *&lt;root&gt;/&lt;subproject&gt;/src/test/resources*:	Test resources
- *&lt;root&gt;/LICENSE.txt*:	Project's license
- *&lt;root&gt;/README.txt*:	Project's readme

Please be noticed that the root project does not have a Gradle build file, only a ```settings.gradle``` file that defines the subprojects to include.

If you are familiar with Maven, the Gradle project layout is mostly indentical to [Maven project](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html).

For more detail on Gradle project layout, please check the following resources:
- [Structuring and Building a Software Component with Gradle](https://docs.gradle.org/7.3.3/userguide/multi_project_builds.html)
- [Organizing Gradle Projects](https://docs.gradle.org/7.3.3/userguide/organizing_gradle_projects.html)

## <a id="gradle_config"></a>Gradle build file setting for EMA Java

While [Apache Maven](https://maven.apache.org/) uses XML for the project configuration, Gradle uses [Groovy](https://groovy-lang.org/) and [Kotlin](https://kotlinlang.org/) domain-specific language for the project configuration instead.

TBD
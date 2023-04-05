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

The RTSDK Java package comes with the Gradle build tool supported by default. However, the included Gradle configuration files are very complex for supporting the SDK multiple APIs (EMA and ETA) and use cases (Core API code, examples, ValueAdd package, etc). If developers want to use Gradle, the RTSDK Java's Gradle settings might not be a good starting point for them.

That is why I am creating this project to show how to use a simple Gradle configuration to work with the EMA Java API. Existing developers who are currently using Gradle also understand how to integrate RTSDK Java into their Gradle project.

Note: 
- This article is based on EMA Java version 3.7.0 L1 (RTSDK Java Edition 2.1.0 L1) and Gradle version 7.3.3 (using Grooy DSL)

## <a id="why_gradle"></a>Why you need build automation tool

The modern Java build automation tools help developers to automate the software build and project management processes including compiling, dependency manager, packing, and running tests. 

If you want to manage the EMA Java application project manually (the old way), you need to manage total **26 jar files** (*as of April 2023*) required by the API as follows:

![Alt text](images/01_jars_files.png)

And developers need to add all jar files to the classpath when running the EMA Java application manually like the following example 

``` bash
$>java -cp .;%EMAJ_HOME%\Libs\ema-<version>.jar;%ETAJ_HOME%\Libs\eta-<version>.jar;%ETAJ_HOME%\Libs\etaValueAdd-<version>.jar;%ETAJ_HOME%\Libs\etaValueAddCache-<version>.jar;%ETAJ_HOME%\Libs\etajConverter-<version>.jar;%ETAJ_HOME%\Libs\ansipage-<version>.jar;%BINPAK%\Ema\Libs\apache\commons-configuration2-2.8.0.jar;%BINPAK%\Ema\Libs\apache\commons-lang3-3.9.jar;%BINPAK%\Ema\Libs\apache\commons-logging-1.2.jar;%BINPAK%\Ema\Libs\apache\commons-text-1.9.jar;%BINPAK%\Ema\Libs\xpp3-1.1.4c.jar;%BINPAK%\Eta\Libs\jackson-annotations-2.14.1.jar;%BINPAK%\Eta\Libs\jackson-core-2.14.1.jar;%BINPAK%\Eta\Libs\jackson-databind-2.14.1.jar;%BINPAK%\Eta\Libs\json-20210307.jar;%BINPAK%\Eta\Libs\lz4-java-1.8.0.jar;%BINPAK%\Eta\Libs\ApacheClient\commons-codec-1.11.jar;%BINPAK%\Eta\Libs\ApacheClient\httpclient-4.5.13.jar;%BINPAK%\Eta\Libs\ApacheClient\httpclient-cache-4.5.13.jar;%BINPAK%\Eta\Libs\ApacheClient\httpcore-4.4.13.jar;%BINPAK%\Eta\Libs\ApacheClient\httpcore-nio-4.4.13.jar;%BINPAK%\Eta\Libs\ApacheClient\httpmime-4.5.13.jar;%BINPAK%\Eta\Libs\jose4j\jose4j-0.9.1.jar;%BINPAK%\Eta\Libs\SLF4J\slf4j-1.7.32\slf4j-api-1.7.32.jar;%BINPAK%\Eta\Libs\SLF4J\slf4j-1.7.32\slf4j-jdk14-1.7.32.jar com.refinitiv.ema.examples.training.consumer.series100.ex100_MP_Streaming.Consumer 
```

The example above is just for running the EMA Java application, the real development project needs to connect to more services,  which means the project needs more jar files, configuration files, etc to manage. This makes the development project hard to set up and hard to collaborate among peers.

The build automation tool can help simplify all of this complexity by helping team manage the project dependencies, and standardize project structure with a simple configuration setting as follows:

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

Before I am going further, there is some prerequisite, dependencies software, and libraries that the project is needed.


### Java SDK

Firstly, you need Java SDK. Please check for the supported Java version from the [API Compatibility Matrix](https://developers.refinitiv.com/en/api-catalog/refinitiv-real-time-opnsrc/rt-sdk-java/documentation#api-compatibility-matrix) page. 

I am using the Open JDK version 11 in this project (as of April 2023).

### Gradle

Next, the [Gradle](https://gradle.org/) build automation tool. Please follow [Gradle installation guide document](https://gradle.org/install/).

### Gradle Wrapper version 7.3.3

Basically, developers use the [Gradle Wrapper](https://docs.gradle.org/7.3.3/userguide/gradle_wrapper.html) to interact with the Gradle build. The Wrapper (```gradlew``` and ```gradlew.bat```) is a script that invokes a declared version of Gradle, downloading it beforehand if necessary. 

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

Please contact your Refinitiv representative to help you with the RTO account and services.

Note: This is for the *CloudConsumer* example only.

### Access to Refinitiv Real-Time Distribution System

Note: This is for the *LocalConsumer* example only. Please contact your Market Data team to help you with the Refinitiv Real-Time Distribution System (RTDS).

## <a id="what_gradle"></a>What is Gradle?

[Gradle](https://gradle.org/) is a multi-language open-source build automation tool. It helps developers and the team to organize the project structure, manage dependencies, and process development tasks such as running the application, packaging/publishing, testing, and much more. The tool is designed for multi-project builds, aims for high performance, supports multiple platforms (runs on the JVM), supports custom tasks and plugins, and fully IDE supported.

Gradle is the official build tool for the Android development platform. 

## <a id="gradle_layout"></a>Project Sturcture 

Gradle is designed for multi-projects development. A recommended structure consists of one root project, and one or more subprojects (the [RTSDK Java](https://github.com/Refinitiv/Real-Time-SDK/tree/master/Java) uses the same structure too). This project folder diagram is as follow:

```
.
├── LICENSE.md
├── README.md
├── ema_app
│   ├── EmaConfig.xml
│   ├── build.gradle
│   ├── etc
│   │   └── ...
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── refinitiv
│       │   │           └── ema
│       │   │               └── examples
│       │   │                   ├── cloudconsumer
│       │   │                   │   └── Consumer.java
│       │   │                   └── localconsumer
│       │   │                       └── Consumer.java
│       │   └── resources
│       │       └── logback.xml
│       └── test
│              └── resources
│                 └── ...
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
└── settings.gradle
```

- *&lt;root&gt;/settings.gradle*: Defined the subprojects to include. 
- *&lt;root&gt;/&lt;subproject&gt;/build.gradle*: Subproject configuration
- *&lt;root&gt;/&lt;subproject&gt;/src/main/java*: Application/Library sources
- *&lt;root&gt;/&lt;subproject&gt;/src/main/resources*: Application/Library resources
- *&lt;root&gt;/&lt;subproject&gt;/src/test/java*:  Test sources
- *&lt;root&gt;/&lt;subproject&gt;/src/test/resources*: Test resources
- *&lt;root&gt;/&lt;subproject&gt;/gradle/*:    Gradle Wrapper (generated from Gradle build)
- *&lt;root&gt;/&lt;subproject&gt;/gradlew*:    Gradle Wrapper start script (generated from Gradle build)
- *&lt;root&gt;/&lt;subproject&gt;/gradlew.bat*:    Gradle Wrapper start script (generated from Gradle build)
- *&lt;root&gt;/LICENSE.txt*:   Project's license
- *&lt;root&gt;/README.txt*:    Project's readme

If you are familiar with Maven, the Gradle project layout is mostly identical to [Maven project](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html).

For more detail on Gradle project layout, please check the following resources:
- [Structuring and Building a Software Component with Gradle](https://docs.gradle.org/7.3.3/userguide/multi_project_builds.html)
- [Organizing Gradle Projects](https://docs.gradle.org/7.3.3/userguide/organizing_gradle_projects.html)

## <a id="gradle_setting"></a>The settings.gradle file

The root project does not have a Gradle build file, it contains only a ```settings.gradle``` file that defines the subprojects to include per Gradle build. The file is generated by the Gradle 'init' task automatically.

This file is actually a [Groovy](https://groovy-lang.org/) Domain-Specific Language (DSL) in the following format:

```
rootProject.name = '<Project name>'
include('sub_project_1')
include('sub_project_2')
```

I am using the following setting for this project:

```
rootProject.name = 'ema_app'
include('ema_app')
```

Note: If the project uses [Kotlin](https://kotlinlang.org/) DSL, this file is ```settings.gradle.kts```

## <a id="gradle_config"></a>Gradle build file setting for EMA Java

Each subproject contains the ```build.gradle``` or ```build.gradle.kts``` file. It is the project's configuration. The file uses [Groovy](https://groovy-lang.org/) (default option) and [Kotlin](https://kotlinlang.org/) DSL format. 

The file contains all project configurations such as project type, Application main class, Java compilation options, etc. The equivalent file in Maven is the ```pom.xml``` file. 

Example:

``` groovy
plugins {
    id 'java'
    id 'application'
   
}

compileJava {
    options.release = 11
}

version = '1.0'

application {
    // Define the main class for the application
    mainClassName = 'com.refinitiv.ema.examples.localconsumer.Consumer'
}

```

The brief information of each ```build.gradle``` configuration function are as follows:
- ```plugins```: Set the project type to Gradle for applying specific features (like compiling Java Code). This is a Java project that creates an executable JVM application, so I am setting *java* and *application* plugins
- ```compileJava```: Set the compiler option. I am setting ```options.release = 11``` for targetting the compiled class(s) to compatible with Java 11
- ```version```: Set the project version 
- ```application```: Set the *application* plugin properties. I am setting the project main class as *com.refinitiv.ema.examples.localconsumer.Consumer* which is based on the EMA Java Consumer 100 example.

You can specify the following EMA Java application dependencies in Gradle build.gradle file. The EMA Java is the message-level API built on top of the ETA Java (Transport API), Gradle can automatically pull all dependency artifacts within Maven central for the application.

``` groovy
plugins {
    id 'java'
    id 'application'  
}
...
repositories {
    mavenCentral()
}

dependencies {
    // This dependency is used by the application.
    implementation 'com.refinitiv.ema:ema:3.7.0.0'
}
```

The ```repositories``` function specifies where to look for the module that we declare as dependencies.  The ```dependencies``` specifies the libraries' IDs and the ```implementation``` property means the project uses EMA for both compilation and runtime.

Please see more detail on the following pages:
- [Build Java and JVM projects](https://docs.gradle.org/7.3.3/userguide/building_java_projects.html)
- [Dependency Management Terminology](https://docs.gradle.org/7.3.3/userguide/dependency_management_terminology.html#dependency_management_terminology)

## <a id="gradle_running"></a>Build and Run the Main Application with Gradle

You can run the *build* task to compile all projects via the following command:

``` Bash
gradlew build
```

The class files will be available in the subproject's *build* folder by default.

Note: Gradle encourages developers to interact with Gradle via the [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html#gradle_wrapper).

Unlike Maven, developers can build and run the main application (the class that has been set via the ```application.mainClassName``` function above) with Gradlde *run* task directly without interacting with class or jar files.

``` Bash
gradlew run
```
Example:

``` Bash
C:\drive_d\Project\Code\EMA_Java_Gradle>gradlew run

> Task :ema_app:run
17:26:49.611 [main] INFO com.refinitiv.ema.access.OmmConsumerImpl -- loggerMsg
    ClientName: ChannelCallbackClient
    Severity: Info
    Text:    Received ChannelUp event on channel Channel_1
        Instance Name Consumer_1_1
        Component Version ads3.6.3.L1.linux.rrg 64-bit
loggerMsgEnd


RefreshMsg
    streamId="5"
    domain="MarketPrice Domain"
    solicited
    RefreshComplete
    state="Open / Ok / None / ''"
    itemGroup="00 01"
    permissionData="03 01 01 36 3c"
    name="/THB="
    nameType="1"
    serviceId="10001"
    serviceName="ELEKTRON_DD"
    Payload dataType="FieldList"
        FieldList FieldListNum="99" DictionaryId="1"
            FieldEntry fid="1" name="PROD_PERM" dataType="UInt" value="363"
            FieldEntry fid="2" name="RDNDISPLAY" dataType="UInt" value="153"
            FieldEntry fid="3" name="DSPLY_NAME" dataType="Rmtes" value="CIMB THAI BK B/d"
            FieldEntry fid="5" name="TIMACT" dataType="Time" value="09:56:00:000:000:000"
            FieldEntry fid="11" name="NETCHNG_1" dataType="Real" value="0.23"
            FieldEntry fid="12" name="HIGH_1" dataType="Real" value="34.46"
            FieldEntry fid="13" name="LOW_1" dataType="Real" value="34.17"
            ....
```
### How to parse parameters to the application. 

My current main class is ```com.refinitiv.ema.examples.localconsumer.Consumer``` which is based on the EMA Java Consumer ex100_MP_Streaming example. The application connects and consumes real-time streaming data with the service *ELEKTRON_DD* from the RTDS or Interactive Provider application in the same machine (*localhost*) by default. However, I have modified it to receive the server IP Address/Hostname, RSSL Port, Service Name, Item Name, and DACS username via command-line arguments. So, how can developers parse parameters to the application via the ```gradlew run``` command? 

You can pass parameters to the run task with ```--args=``` option as follows:

``` Bash 
gradlew run --args="-service IDN_RDF -itemName AAPL.O"
```
To clean up the build, you can just run a ```gradlew clean``` command to can delete the contents of the build directory.

Please find more detail about Gradle command-line interface from [Gradle page](https://docs.gradle.org/7.3.3/userguide/command_line_interface.html#sec:command_line_completion).

## <a id="gradle_running"></a>Build and Run Other Java Tasks

Running the main application with the ```gradlew run``` command is convenient. However, developers may need to run mutiple applications to start or test other services. It is not practical to keep changing the main class in ```application.mainClassName``` function of a ```build.gradle``` file. Luckyly, Gradle lets developer create custom tasks for various propose, including run others Java class.

The best example is the official RTSDK Java Gradle project that supports various SDK samples such as  ```gradlew runconsumer100```, ```gradlew runiprovider180```, ```gradlew runVAConsumer```, etc.   


``` Groovy
task runCloudConsumer(type: JavaExec) {

    dependsOn("compileJava")
    classpath = sourceSets.main.runtimeClasspath
    
    mainClass = 'com.refinitiv.ema.examples.cloudconsumer.Consumer'

    // arguments to pass to the application
    args 'appArg1'
}
```


TB
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

This example project shows how to create the EMA Java project with a simple Gradle configuration. 

## <a id="rebrand"></a>IMPORTANT Rebranding Announcement: 

Starting with version RTSDK 2.0.0.L1 (same as EMA/ETA 3.6.0.L1), there are namespace changes and library name changes. Please note that all interfaces remain the same as prior releases of RTSDK and Elektron SDK and will remain fully wire compatible. Along with RTSDK 2.X version, a [REBRAND.md](https://github.com/Refinitiv/Real-Time-SDK/blob/master/REBRAND.md) is published to detail the impact to existing applications and how to quickly adapt to the re-branded libraries. Existing applications will continue to work indefinitely as-is.  Applications should be proactively rebranded to be able to utilize new features, security updates, or fixes post 2.X release. Please see [PCN](https://my.refinitiv.com/content/mytr/en/pcnpage/12072.html?_ga=2.103280071.632863608.1606731450-325683966.1598503157) for more details on support. 

## <a id="why_gradle"></a>Why you need build automation tool

The modern Java build automation tools help developers to automate the software build and project management processes including compiling, dependency manager, packing, and running tests. 

If you want to manage the EMA Java application project manually (the old way), you need to manage total **26 jar files** (*as of April 2023*) required by the API as follows:

![Alt text](images/01_jars_files.png)

And developers need to add all jar files to the classpath when running the EMA Java application manually like the following example 

``` bash
$>java -cp .;%EMAJ_HOME%\Libs\ema-<version>.jar;%ETAJ_HOME%\Libs\eta-<version>.jar;%ETAJ_HOME%\Libs\etaValueAdd-<version>.jar;%ETAJ_HOME%\Libs\etaValueAddCache-<version>.jar;%ETAJ_HOME%\Libs\etajConverter-<version>.jar;%ETAJ_HOME%\Libs\ansipage-<version>.jar;%BINPAK%\Ema\Libs\apache\commons-configuration2-2.8.0.jar;%BINPAK%\Ema\Libs\apache\commons-lang3-3.9.jar;%BINPAK%\Ema\Libs\apache\commons-logging-1.2.jar;%BINPAK%\Ema\Libs\apache\commons-text-1.9.jar;%BINPAK%\Ema\Libs\xpp3-1.1.4c.jar;%BINPAK%\Eta\Libs\jackson-annotations-2.14.1.jar;%BINPAK%\Eta\Libs\jackson-core-2.14.1.jar;%BINPAK%\Eta\Libs\jackson-databind-2.14.1.jar;%BINPAK%\Eta\Libs\json-20210307.jar;%BINPAK%\Eta\Libs\lz4-java-1.8.0.jar;%BINPAK%\Eta\Libs\ApacheClient\commons-codec-1.11.jar;%BINPAK%\Eta\Libs\ApacheClient\httpclient-4.5.13.jar;%BINPAK%\Eta\Libs\ApacheClient\httpclient-cache-4.5.13.jar;%BINPAK%\Eta\Libs\ApacheClient\httpcore-4.4.13.jar;%BINPAK%\Eta\Libs\ApacheClient\httpcore-nio-4.4.13.jar;%BINPAK%\Eta\Libs\ApacheClient\httpmime-4.5.13.jar;%BINPAK%\Eta\Libs\jose4j\jose4j-0.9.1.jar;%BINPAK%\Eta\Libs\SLF4J\slf4j-1.7.32\slf4j-api-1.7.32.jar;%BINPAK%\Eta\Libs\SLF4J\slf4j-1.7.32\slf4j-jdk14-1.7.32.jar com.refinitiv.ema.examples.training.consumer.series100.ex100_MP_Streaming.Consmer 
```

The example above is just for running the EMA Java application, the real development project needs to connect to more services,  which means the project needs more jar files, configuration files, etc to manage. This makes the development project hard to set up and hard to collaborate among peers.

The build automation tool can help simplify all of this complexity.

## <a id="what_gradle"></a>What is Gradle?

TBD
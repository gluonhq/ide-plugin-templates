<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>${packageName}</groupId>
    <artifactId>${projectName?lower_case?replace("\\s+", "-", "r")}</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>${projectName}</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.release>11</maven.compiler.release>
        <javafx.version>${javafxVersion}</javafx.version>
        <attach.version>${attachVersion}</attach.version>
        <gluonfx.plugin.version>${gluonfxMavenPlugin}</gluonfx.plugin.version>
        <javafx.plugin.version>${javafxMavenPlugin}</javafx.plugin.version>
        <mainClassName>${mainClass}</mainClassName>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${r"${javafx.version}"}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>${r"${javafx.version}"}</version>
        </dependency>
        <dependency>
            <groupId>com.gluonhq</groupId>
            <artifactId>charm-glisten</artifactId>
            <version>${glistenVersion}</version>
        </dependency>
        <dependency>
            <groupId>com.gluonhq.attach</groupId>
            <artifactId>display</artifactId>
            <version>${r"${attach.version}"}</version>
        </dependency>
        <dependency>
            <groupId>com.gluonhq.attach</groupId>
            <artifactId>lifecycle</artifactId>
            <version>${r"${attach.version}"}</version>
        </dependency>
        <dependency>
            <groupId>com.gluonhq.attach</groupId>
            <artifactId>statusbar</artifactId>
            <version>${r"${attach.version}"}</version>
        </dependency>
        <dependency>
            <groupId>com.gluonhq.attach</groupId>
            <artifactId>storage</artifactId>
            <version>${r"${attach.version}"}</version>
        </dependency>
        <dependency>
            <groupId>com.gluonhq.attach</groupId>
            <artifactId>util</artifactId>
            <version>${r"${attach.version}"}</version>
        </dependency>
        <#if afterburnerEnabled>
        <dependency>
            <groupId>com.airhacks</groupId>
            <artifactId>afterburner.mfx</artifactId>
            <version>1.6.3</version>
        </dependency>
        <dependency>
            <groupId>javax.annotation</groupId>
            <artifactId>javax.annotation-api</artifactId>
            <version>1.3.2</version>
        </dependency>
        </#if>
    </dependencies>

    <repositories>
        <repository>
            <id>Gluon</id>
            <url>https://nexus.gluonhq.com/nexus/content/repositories/releases</url>
        </repository>
    </repositories>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
            </plugin>

            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>${r"${javafx.plugin.version}"}</version>
                <configuration>
                    <mainClass>${r"${mainClassName}"}</mainClass>
                </configuration>
<#if ide == "netbeans">
                <executions>
                    <execution>
                        <!-- Default configuration for running -->
                        <!-- Usage: mvn clean javafx:run -->
                        <id>default-cli</id>
                    </execution>
                    <execution>
                        <!-- Configuration for manual attach debugging -->
                        <!-- Usage: mvn clean javafx:run@debug -->
                        <id>debug</id>
                        <configuration>
                            <options>
                                <option>-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=localhost:8000</option>
                            </options>
                        </configuration>
                    </execution>
                    <execution>
                        <!-- Configuration for automatic IDE debugging -->
                        <id>ide-debug</id>
                        <configuration>
                            <options>
                                <option>-agentlib:jdwp=transport=dt_socket,server=n,address=${r"${jpda.address}"}</option>
                            </options>
                        </configuration>
                    </execution>
                    <execution>
                        <!-- Configuration for automatic IDE profiling -->
                        <id>ide-profile</id>
                        <configuration>
                            <options>
                                <option>${r"${profiler.jvmargs.arg1}"}</option>
                                <option>${r"${profiler.jvmargs.arg2}"}</option>
                                <option>${r"${profiler.jvmargs.arg3}"}</option>
                                <option>${r"${profiler.jvmargs.arg4}"}</option>
                                <option>${r"${profiler.jvmargs.arg5}"}</option>
                            </options>
                        </configuration>
                    </execution>
                </executions>
</#if>
            </plugin>

            <plugin>
                <groupId>com.gluonhq</groupId>
                <artifactId>gluonfx-maven-plugin</artifactId>
                <version>${r"${gluonfx.plugin.version}"}</version>
                <configuration>
                    <target>${r"${gluonfx.target}"}</target>
                    <attachList>
                        <list>display</list>
                        <list>lifecycle</list>
                        <list>statusbar</list>
                        <list>storage</list>
                    </attachList>
                    <reflectionList>
                        <list>${packageName}.views.${primaryViewName}Presenter</list>
                        <list>${packageName}.views.${secondaryViewName}Presenter</list>
                    </reflectionList>
                    <mainClass>${r"${mainClassName}"}</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <profiles>
        <profile>
            <id>ios</id>
            <properties>
                <gluonfx.target>ios</gluonfx.target>
            </properties>
        </profile>
        <profile>
            <id>android</id>
            <properties>
                <gluonfx.target>android</gluonfx.target>
            </properties>
        </profile>
    </profiles>
</project>

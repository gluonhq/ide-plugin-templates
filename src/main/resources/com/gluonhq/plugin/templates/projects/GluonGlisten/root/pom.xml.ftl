<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.gluonhq</groupId>
    <artifactId>single-view-example</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>HelloGluon</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <javafx.version>13.0.1</javafx.version>
        <attach.version>${attachVersion}</attach.version>
        <client.plugin.version>${clientMavenPlugin}</client.plugin.version>
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
            <groupId>com.gluonhq</groupId>
            <artifactId>charm-glisten</artifactId>
            <version>${mobileVersion}</version>
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
    </dependencies>

    <repositories>
        <repository>
            <id>Gluon</id>
            <url>https://nexus.gluonhq.com/nexus/content/repositories/releases</url>
        </repository>
    </repositories>

    <pluginRepositories>
        <pluginRepository>
            <id>gluon-releases</id>
            <url>https://nexus.gluonhq.com/nexus/content/repositories/releases/</url>
        </pluginRepository>
    </pluginRepositories>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <release>11</release>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>${r"${javafx.plugin.version}"}</version>
                <configuration>
                    <mainClass>${r"${mainClassName}"}</mainClass>
                </configuration>
            </plugin>

            <plugin>
                <groupId>com.gluonhq</groupId>
                <artifactId>client-maven-plugin</artifactId>
                <version>${r"${client.plugin.version}"}</version>
                <configuration>
                    <target>${r"${client.target}"}</target>
                    <attachList>
                        <list>display</list>
                        <list>lifecycle</list>
                        <list>statusbar</list>
                        <list>storage</list>
                    </attachList>
                    <mainClass>${r"${mainClassName}"}</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <profiles>
        <profile>
            <id>desktop</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <client.target>host</client.target>
            </properties>
            <dependencies>
                <dependency>
                    <groupId>com.gluonhq.attach</groupId>
                    <artifactId>display</artifactId>
                    <version>${r"${attach.version}"}</version>
                    <classifier>desktop</classifier>
                </dependency>
                <dependency>
                    <groupId>com.gluonhq.attach</groupId>
                    <artifactId>lifecycle</artifactId>
                    <version>${r"${attach.version}"}</version>
                    <classifier>desktop</classifier>
                </dependency>
                <dependency>
                    <groupId>com.gluonhq.attach</groupId>
                    <artifactId>storage</artifactId>
                    <version>${r"${attach.version}"}</version>
                    <classifier>desktop</classifier>
                </dependency>
            </dependencies>
        </profile>
        <profile>
            <id>ios</id>
            <properties>
                <client.target>ios</client.target>
            </properties>
        </profile>
        <profile>
            <id>android</id>
            <properties>
                <client.target>android</client.target>
            </properties>
        </profile>
    </profiles>
</project>
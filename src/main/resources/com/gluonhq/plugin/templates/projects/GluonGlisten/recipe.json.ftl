[
<#-- root -->
<#if buildTool == "maven">
  {
    "command": "process",
    "from": "pom.xml.ftl",
    "to": "./pom.xml"
  },
<#elseif buildTool == "gradle">
  {
    "command": "process",
    "from": "build.gradle.ftl",
    "to": "./build.gradle"
  },
  {
    "command": "copylist",
    "list": "gradle_wrapper.lst",
    "base": "/gradle/wrapper",
    "to": "./"
  },
  {
    "command": "permission",
    "at": "./gradlew"
  },
</#if>
<#-- app -->
  {
    "command": "mkdir",
    "at": "./src/main/java"
  },
  {
    "command": "mkdir",
    "at": "./src/main/resources"
  }
]
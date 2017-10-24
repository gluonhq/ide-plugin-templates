[
<#-- fn -->
  {
    "command": "mkdir",
    "at": "./${projectNameFn}"
  },
  {
    "command": "process",
    "from": "build.gradle.ftl",
    "to": "./${projectNameFn}/build.gradle"
  },
  {
    "command": "process",
    "from": "gradle.properties.ftl",
    "to": "./${projectNameFn}/gradle.properties"
  },
  {
    "command": "mkdir",
    "at": "./${projectNameFn}/src/main/java"
  }
]
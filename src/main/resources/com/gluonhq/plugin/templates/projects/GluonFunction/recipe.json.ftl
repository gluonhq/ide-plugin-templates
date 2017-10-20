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
    "command": "mkdir",
    "at": "./${projectNameFn}/src/main/java"
  }
]
[
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
  {
    "command": "mkdir",
    "at": "./src/main/java"
  },
  {
    "command": "mkdir",
    "at": "./src/main/resources"
  }
<#if androidEnabled>
  ,{
    "command": "mkdir",
    "at": "./src/android/java"
  },
  {
    "command": "mkdir",
    "at": "./src/android/resources"
  },
  {
    "command": "mkdir",
    "at": "./src/android/assets"
  },
  {
    "command": "mkdir",
    "at": "./src/android/res"
  },
  {
    "command": "process",
    "from": "AndroidManifest.xml.ftl",
    "to": "./src/android/AndroidManifest.xml"
  },
  {
    "command": "copylist",
    "list": "android_res.lst",
    "base": "/android/res",
    "to": "./src/android/res"
  }
</#if>
<#if iosEnabled>
  ,{
    "command": "mkdir",
    "at": "./src/ios/java"
  },
  {
    "command": "mkdir",
    "at": "./src/ios/resources"
  },
  {
    "command": "mkdir",
    "at": "./src/ios/assets"
  },
  {
    "command": "process",
    "from": "Info.plist.ftl",
    "to": "./src/ios/Default-Info.plist"
  },
  {
    "command": "copylist",
    "list": "ios_assets.lst",
    "base": "/ios/assets",
    "to": "./src/ios/assets"
  }
</#if>
<#if desktopEnabled>
  ,{
    "command": "mkdir",
    "at": "./src/desktop/java"
  },
  {
    "command": "mkdir",
    "at": "./src/desktop/resources"
  },
  {
    "command": "copy",
    "from": "/desktop/icon.png",
    "to": "./src/main/resources/icon.png"
  }
</#if>
<#if embeddedEnabled>
  ,{
    "command": "mkdir",
    "at": "./src/embedded/java"
  },
  {
    "command": "mkdir",
    "at": "./src/embedded/resources"
  }
</#if>
]
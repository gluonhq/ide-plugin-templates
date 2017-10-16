[
<#-- root -->
  {
    "command": "process",
    "from": "/root/build.gradle.ftl",
    "to": "./build.gradle"
  },
  {
    "command": "process",
    "from": "/root/settings.gradle.ftl",
    "to": "./settings.gradle"
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
<#-- app -->
  {
    "command": "mkdir",
    "at": "./${projectNameApp}"
  },
  {
    "command": "process",
    "from": "build.gradle.ftl",
    "to": "./${projectNameApp}/build.gradle"
  },
  {
    "command": "mkdir",
    "at": "./${projectNameApp}/src/main/java"
  },
  {
    "command": "mkdir",
    "at": "./${projectNameApp}/src/main/resources"
  },
  {
    "command": "copy",
    "from": "/desktop/icon.png",
    "to": "./${projectNameApp}/src/main/resources/icon.png"
  }
<#if androidEnabled>
  ,{
    "command": "mkdir",
    "at": "./${projectNameApp}/src/android/java"
  },
  {
    "command": "mkdir",
    "at": "./${projectNameApp}/src/android/resources"
  },
  {
    "command": "mkdir",
    "at": "./${projectNameApp}/src/android/assets"
  },
  {
    "command": "mkdir",
    "at": "./${projectNameApp}/src/android/res"
  },
  {
    "command": "process",
    "from": "AndroidManifest.xml.ftl",
    "to": "./${projectNameApp}/src/android/AndroidManifest.xml"
  },
  {
    "command": "copylist",
    "list": "android_res.lst",
    "base": "/android/res",
    "to": "./${projectNameApp}/src/android/res"
  }
</#if>
<#if iosEnabled>
  ,{
    "command": "mkdir",
    "at": "./${projectNameApp}/src/ios/java"
  },
  {
    "command": "mkdir",
    "at": "./${projectNameApp}/src/ios/resources"
  },
  {
    "command": "mkdir",
    "at": "./${projectNameApp}/src/ios/assets"
  },
  {
    "command": "process",
    "from": "Info.plist.ftl",
    "to": "./${projectNameApp}/src/ios/Default-Info.plist"
  },
  {
    "command": "copylist",
    "list": "ios_assets.lst",
    "base": "/ios/assets",
    "to": "./${projectNameApp}/src/ios/assets"
  }
</#if>
<#if desktopEnabled>
  ,{
    "command": "mkdir",
    "at": "./${projectNameApp}/src/desktop/java"
  },
  {
    "command": "mkdir",
    "at": "./${projectNameApp}/src/desktop/resources"
  }
</#if>
<#if embeddedEnabled>
  ,{
    "command": "mkdir",
    "at": "./${projectNameApp}/src/embedded/java"
  },
  {
    "command": "mkdir",
    "at": "./${projectNameApp}/src/embedded/resources"
  }
</#if>
]
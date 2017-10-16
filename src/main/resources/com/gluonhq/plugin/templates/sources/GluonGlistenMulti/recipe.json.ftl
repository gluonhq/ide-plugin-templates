[
  {
    "command": "process",
    "open": true,
    "from": "GlistenApplication.java.ftl",
    "to": "./${projectNameApp}/src/main/java/${packageFolder}/${mainClassName}.java"
  },
  {
    "command": "process",
    "open": false,
    "from": "DrawerManager.java.ftl",
    "to": "./${projectNameApp}/src/main/java/${packageFolder}/DrawerManager.java"
  },
  {
    "command": "process",
    "open": false,
    "from": "PrimaryView.java.ftl",
    "to": "./${projectNameApp}/src/main/java/${packageFolder}/views/${primaryViewName}View.java"
  },
  {
    "command": "process",
    "open": false,
    "from": "SecondaryView.java.ftl",
    "to": "./${projectNameApp}/src/main/java/${packageFolder}/views/${secondaryViewName}View.java"
  }
<#if cssProjectEnabled>
, {
    "command": "process",
    "open": false,
    "from": "root.css.ftl",
    "to": "./${projectNameApp}/src/main/resources/${packageFolder}/style.css"
  }
</#if>
<#if cssPrimaryViewEnabled>
, {
    "command": "process",
    "open": false,
    "from": "style.css.ftl",
    "to": "./${projectNameApp}/src/main/resources/${packageFolder}/views/${primaryCSSName}.css"
  }
</#if>
<#if cssSecondaryViewEnabled>
, {
    "command": "process",
    "open": false,
    "from": "style.css.ftl",
    "to": "./${projectNameApp}/src/main/resources/${packageFolder}/views/${secondaryCSSName}.css"
  }
</#if>
]
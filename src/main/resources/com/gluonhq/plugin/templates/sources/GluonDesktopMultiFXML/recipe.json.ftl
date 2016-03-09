[
  {
    "command": "process",
    "open": true,
    "from": "DesktopApplication.java.ftl",
    "to": "./src/main/java/${packageFolder}/${mainClassName}.java"
  },
  {
    "command": "process",
    "open": false,
    "from": "PrimaryView.java.ftl",
    "to": "./src/main/java/${packageFolder}/views/${primaryViewName}View.java"
  },
  {
    "command": "process",
    "open": false,
    "from": "PrimaryController.java.ftl",
    "to": "./src/main/java/${packageFolder}/controllers/${primaryViewName}Controller.java"
  },
  {
    "command": "process",
    "open": false,
    "from": "SecondaryView.java.ftl",
    "to": "./src/main/java/${packageFolder}/views/${secondaryViewName}View.java"
  },
  {
    "command": "process",
    "open": false,
    "from": "SecondaryController.java.ftl",
    "to": "./src/main/java/${packageFolder}/controllers/${secondaryViewName}Controller.java"
  },
  {
    "command": "process",
    "open": false,
    "from": "MenuActions.java.ftl",
    "to": "./src/main/java/${packageFolder}/actions/MenuActions.java"
  },
  {
    "command": "process",
    "open": false,
    "from": "primary.fxml.ftl",
    "to": "./src/main/resources/${packageFolder}/views/${primaryCSSName}.fxml"
  },
  {
    "command": "process",
    "open": false,
    "from": "secondary.fxml.ftl",
    "to": "./src/main/resources/${packageFolder}/views/${secondaryCSSName}.fxml"
  },
  {
    "command": "process",
    "open": false,
    "from": "primary.properties.ftl",
    "to": "./src/main/resources/${packageFolder}/views/${primaryCSSName}.properties"
  },
  {
    "command": "process",
    "open": false,
    "from": "secondary.properties.ftl",
    "to": "./src/main/resources/bundles/${secondaryCSSName}.properties"
  }
<#if cssProjectEnabled>
, {
    "command": "process",
    "open": false,
    "from": "style.css.ftl",
    "to": "./src/main/resources/${packageFolder}/style.css"
  }
</#if>
<#if cssPrimaryViewEnabled>
, {
    "command": "process",
    "open": false,
    "from": "view.css.ftl",
    "to": "./src/main/resources/${packageFolder}/views/${primaryCSSName}.css"
  }
</#if>
<#if cssSecondaryViewEnabled>
, {
    "command": "process",
    "open": false,
    "from": "view.css.ftl",
    "to": "./src/main/resources/${packageFolder}/views/${secondaryCSSName}.css"
  }
</#if>
]

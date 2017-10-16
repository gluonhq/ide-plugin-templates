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
    "from": "AppViewManager.java.ftl",
    "to": "./${projectNameApp}/src/main/java/${packageFolder}/views/AppViewManager.java"
  },
  {
    "command": "process",
    "open": false,
    "from": "PrimaryPresenter.java.ftl",
    "to": "./${projectNameApp}/src/main/java/${packageFolder}/views/${primaryViewName}Presenter.java"
  },
  {
    "command": "process",
    "open": false,
    "from": "SecondaryPresenter.java.ftl",
    "to": "./${projectNameApp}/src/main/java/${packageFolder}/views/${secondaryViewName}Presenter.java"
  },
  {
    "command": "process",
    "open": false,
    "from": "primary.fxml.ftl",
    "to": "./${projectNameApp}/src/main/resources/${packageFolder}/views/${primaryCSSName}.fxml"
  },
  {
    "command": "process",
    "open": false,
    "from": "secondary.fxml.ftl",
    "to": "./${projectNameApp}/src/main/resources/${packageFolder}/views/${secondaryCSSName}.fxml"
  }
<#if cssProjectEnabled>
, {
    "command": "process",
    "open": false,
    "from": "root.css.ftl",
    "to": "./${projectNameApp}/src/main/resources/${packageFolder}/style.css"
  }
</#if>
, {
    "command": "process",
    "open": false,
    "from": "style.css.ftl",
    "to": "./${projectNameApp}/src/main/resources/${packageFolder}/views/${primaryCSSName}.css"
  },
  {
    "command": "process",
    "open": false,
    "from": "primary.properties.ftl",
    "to": "./${projectNameApp}/src/main/resources/${packageFolder}/views/${primaryCSSName}.properties"
  },
  {
    "command": "process",
    "open": false,
    "from": "style.css.ftl",
    "to": "./${projectNameApp}/src/main/resources/${packageFolder}/views/${secondaryCSSName}.css"
  },
  {
    "command": "process",
    "open": false,
    "from": "secondary.properties.ftl",
    "to": "./${projectNameApp}/src/main/resources/${packageFolder}/views/${secondaryCSSName}.properties"
  }
]
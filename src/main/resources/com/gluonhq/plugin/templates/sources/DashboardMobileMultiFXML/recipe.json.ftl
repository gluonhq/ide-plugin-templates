[
  {
    "command": "process",
    "open": true,
    "from": "GlistenApplication.java.ftl",
    "to": "./src/main/java/${packageFolder}/${mainClassName}.java"
  },
  {
    "command": "process",
    "open": false,
    "from": "DrawerManager.java.ftl",
    "to": "./src/main/java/${packageFolder}/DrawerManager.java"
  }
<#list views as view>
, {
    "command": "process",
    "open": false,
    "from": "View.java.ftl",
    "to": "./src/main/java/${packageFolder}/views/${view.name}View.java",
    "loop_index": ${view?index}
  },
  {
    "command": "process",
    "open": false,
    "from": "Presenter.java.ftl",
    "to": "./src/main/java/${packageFolder}/views/${view.name}Presenter.java",
    "loop_index": ${view?index}
  },
  {
    "command": "process",
    "open": false,
    "from": "view.fxml.ftl",
    "to": "./src/main/resources/${packageFolder}/views/${view.cssName}.fxml",
    "loop_index": ${view?index}
  }
<#if view.createCss>
, {
    "command": "process",
    "open": false,
    "from": "style.css.ftl",
    "to": "./src/main/resources/${packageFolder}/views/${view.cssName}.css",
    "loop_index": ${view?index}
  }
</#if>
</#list>
<#if cssProjectEnabled>
, {
    "command": "process",
    "open": false,
    "from": "root.css.ftl",
    "to": "./src/main/resources/${packageFolder}/style.css"
  }
</#if>
]
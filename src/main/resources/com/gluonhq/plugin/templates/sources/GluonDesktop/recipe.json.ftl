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
    "from": "BasicView.java.ftl",
    "to": "./src/main/java/${packageFolder}/view/BasicView.java"
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
    "from": "ToolBarActions.java.ftl",
    "to": "./src/main/java/${packageFolder}/actions/ToolBarActions.java"
  },
  {
    "command": "process",
    "open": false,
    "from": "style.css.ftl",
    "to": "./src/main/resources/${packageFolder}/style.css"
  }
]

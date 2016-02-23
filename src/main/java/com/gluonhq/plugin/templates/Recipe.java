package com.gluonhq.plugin.templates;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private static final String KEY_COMMAND = "command";

    private static final String COMMAND_COPY = "copy";
    private static final String COMMAND_COPYLIST = "copylist";
    private static final String COMMAND_MKDIR = "mkdir";
    private static final String COMMAND_PROCESS = "process";

    private List<Command> commands = new ArrayList<>();

    public static Recipe parse(Reader reader) {
        Recipe recipe = new Recipe();

        JsonArray jsonCommands = Json.createReader(reader).readArray();
        for (int i = 0; i < jsonCommands.size(); i++) {
            JsonObject jsonCommand = jsonCommands.getJsonObject(i);
            String command = jsonCommand.getString(KEY_COMMAND);
            switch (command) {
                case COMMAND_COPY:
                    recipe.addCommand(new CopyCommand(jsonCommand));
                    break;
                case COMMAND_COPYLIST:
                    recipe.addCommand(new CopyListCommand(jsonCommand));
                    break;
                case COMMAND_PROCESS:
                    recipe.addCommand(new ProcessCommand(jsonCommand));
                    break;
                case COMMAND_MKDIR:
                    recipe.addCommand(new MkdirCommand(jsonCommand));
                    break;
            }
        }

        return recipe;
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public void execute(RecipeContext recipeContext) {
        for (Command command : commands) {
            command.execute(recipeContext);
        }
    }
}

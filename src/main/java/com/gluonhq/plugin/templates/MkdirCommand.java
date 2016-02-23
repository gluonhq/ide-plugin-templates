package com.gluonhq.plugin.templates;

import javax.json.JsonObject;
import java.io.File;

public class MkdirCommand extends Command {

    private File at;

    public MkdirCommand(JsonObject jsonCommand) {
        at = new File(jsonCommand.getString("at"));
    }

    @Override
    public void execute(RecipeContext recipeContext) {
        recipeContext.mkdir(at);
    }

}

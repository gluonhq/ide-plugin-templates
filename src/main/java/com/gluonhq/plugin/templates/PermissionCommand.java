package com.gluonhq.plugin.templates;

import javax.json.JsonObject;
import java.io.File;

public class PermissionCommand extends Command {

    private File at;

    public PermissionCommand(JsonObject jsonCommand) {
        at = new File(jsonCommand.getString("at"));
    }

    @Override
    public void execute(RecipeContext recipeContext) {
        recipeContext.permission(at);
    }

}

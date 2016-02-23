package com.gluonhq.plugin.templates;

import javax.json.JsonObject;
import java.io.File;

public class CopyListCommand extends Command {

    private String list;
    private String base;
    private File to;

    public CopyListCommand(JsonObject jsonCommand) {
        list = jsonCommand.getString("list");
        base = jsonCommand.getString("base");
        to = new File(jsonCommand.getString("to"));
    }

    @Override
    public void execute(RecipeContext recipeContext) {
        recipeContext.copyList(list, base, to);
    }

}

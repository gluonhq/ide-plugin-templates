package com.gluonhq.plugin.templates;

import javax.json.JsonObject;
import java.io.File;

public class ProcessCommand extends Command {

    private boolean open;
    private String from;
    private File to;

    public ProcessCommand(JsonObject jsonCommand) {
        open = jsonCommand.containsKey("open") && jsonCommand.getBoolean("open", false);
        from = jsonCommand.getString("from");
        to = new File(jsonCommand.getString("to"));
    }

    @Override
    public void execute(RecipeContext recipeContext) {
        recipeContext.process(from, to, open);
    }

}

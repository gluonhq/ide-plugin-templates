package com.gluonhq.plugin.templates;

import javax.json.JsonObject;
import java.io.File;

public class ProcessCommand extends Command {

    private boolean open;
    private String from;
    private File to;
    private int loop_index;

    public ProcessCommand(JsonObject jsonCommand) {
        open = jsonCommand.getBoolean("open", false);
        from = jsonCommand.getString("from");
        to = new File(jsonCommand.getString("to"));
        loop_index = jsonCommand.getInt("loop_index", -1);
    }

    @Override
    public void execute(RecipeContext recipeContext) {
        recipeContext.getParameters().put("loop_index", loop_index);
        recipeContext.process(from, to, open);
    }

}

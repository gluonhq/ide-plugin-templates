/*
 * Copyright (c) 2016, 2022, Gluon Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
    private static final String COMMAND_PERMISSION = "permission";

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
                case COMMAND_PERMISSION:
                    recipe.addCommand(new PermissionCommand(jsonCommand));
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

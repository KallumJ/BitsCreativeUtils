package team.bits.creative.utils.commands;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

import java.util.ArrayList;

public class Commands {

    public static final ArrayList<Command> COMMANDS_LIST = new ArrayList<>();

    static {
        addCommand(new NightVisionCommand());
    }

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            for (Command command : COMMANDS_LIST) {
                command.registerCommand(dispatcher);
            }
        });
    }

    private static void addCommand(Command command) {
        COMMANDS_LIST.add(command);
    }
}

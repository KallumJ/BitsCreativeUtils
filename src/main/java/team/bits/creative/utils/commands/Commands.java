package team.bits.creative.utils.commands;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import team.bits.nibbles.command.Command;
import team.bits.nibbles.command.CommandManager;


import java.util.ArrayList;

public class Commands {

    public static final ArrayList<Command> COMMANDS_LIST = new ArrayList<>();

    public static void registerCommands() {
        addCommand(new NightVisionCommand());
    }

    private static void addCommand(Command command) {
        CommandManager.INSTANCE.register(command);
        COMMANDS_LIST.add(command);
    }
}

package team.bits.creative.utils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.shape.BitSetVoxelSet;
import team.bits.creative.utils.BitsCreativeUtils;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.server.command.CommandManager.literal;

public class FlightSpeedCommand extends Command {
    public static final Map<ServerPlayerEntity, Float> INCREASED_SPEED_PLAYERS = new HashMap<>();

    public FlightSpeedCommand() {
        super("flightspeed", new String[]{"fs"}, new CommandInformation()
                .setDescription("Toggles very high speed or sets the speed to the specified speed")
                .setUsage("[speed (1-10)]").setPublic(true)
        );
    }

    @Override
    public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        CommandNode<ServerCommandSource> commandNode =
                dispatcher.register(
                        literal(super.getName())
                                .then(CommandManager.argument("speed", IntegerArgumentType.integer())
                                        .executes(this)
                                        .suggests(CommandSuggestionUtils.INTEGERS_1_TO_10)
                                ));

        super.registerAliases(dispatcher, commandNode);
    }

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        //private float flySpeed = 0.05F; DEFAULT FLY SPEED

        ServerPlayerEntity player = context.getSource().getPlayer();
        int speedArg = context.getArgument("speed", Integer.class);

        BitsCreativeUtils.audience(player).sendMessage(Component.text(speedArg));

        return 1;
    }

    private float getSpeedFromArgument(int arg) {
        return (float) ((float) arg / 100.0 * 5);
    }
}

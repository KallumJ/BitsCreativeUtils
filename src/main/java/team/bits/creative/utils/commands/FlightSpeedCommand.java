package team.bits.creative.utils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import team.bits.nibbles.command.Command;
import team.bits.nibbles.command.CommandInformation;
import team.bits.nibbles.utils.Colors;
import team.bits.nibbles.utils.MessageTypes;

import static net.minecraft.server.command.CommandManager.literal;

public class FlightSpeedCommand extends Command {
    private static final String INVALID_ARG_MSG = "Please enter a speed value between 1 and 10";
    private static final String ADJUSTING_SPEED_MSG = "Adjusting speed!";
    private static final String RESETTING_SPEED_MSG = "Resetting speed!";

    private static final float MAX_SPEED = getSpeedFromArgument(10);
    private static final float DEFAULT_SPEED = getSpeedFromArgument(1);

    public FlightSpeedCommand() {
        super("flightspeed", new CommandInformation()
                .setDescription("Toggles very high speed or sets the speed to the specified speed")
                .setUsage("[speed (1-10)]").setPublic(true),
                "fs"
        );
    }

    @Override
    public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        CommandNode<ServerCommandSource> commandNode =
                dispatcher.register(
                        literal(super.getName())
                                .executes(this)
                                .then(CommandManager.argument("speed", IntegerArgumentType.integer())
                                        .executes(this::setSpeed)
                                        .suggests(CommandSuggestionUtils.INTEGERS_1_TO_10)
                                ));

        dispatcher.register(
                literal("fs")
                        .executes(this)
                        .redirect(commandNode)
        );
    }

    public int setSpeed(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        //private float flySpeed = 0.05F; DEFAULT FLY SPEED

        ServerPlayerEntity player = context.getSource().getPlayer();
        int speedArg = context.getArgument("speed", Integer.class);

        // If provided argument is between 1 and 10, set their speed
        if (speedArg >= 1 && speedArg <= 10) {
            setSpeed(player, getSpeedFromArgument(speedArg));
            player.sendMessage(Text.literal(ADJUSTING_SPEED_MSG), MessageTypes.POSITIVE);
        } else {
            throw new SimpleCommandExceptionType(() -> INVALID_ARG_MSG).create();
        }

        return 1;
    }

    // Default flight speed is 0.05. Min argument is 1. This will return 0.05 if you pass 1,
    // and then higher arguments will scale accordingly, e.g, 2 should be double original speed, returns 0.1
    private static float getSpeedFromArgument(int arg) {
        return (float) ((float) arg / 100.0 * 5);
    }

    // This takes a speed value, and returns what argument would've been passed to create that speed value
    // e.g, passing 0.05 will return 1.
    private static int getArgumentFromSpeed(float speed) {
        return (int) (speed * 100 / 5);
    }

    private void setSpeed(ServerPlayerEntity player, float speed) {
        player.getAbilities().setFlySpeed(speed);
        player.sendAbilitiesUpdate();
    }

    // Toggle speed
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();

        int speedArg = getArgumentFromSpeed(player.getAbilities().getFlySpeed());

        // If player is already speedy
        if (speedArg > 1) {
            // Reset them to default speed
            setSpeed(player, DEFAULT_SPEED);
            player.sendMessage(Text.literal(RESETTING_SPEED_MSG), MessageTypes.POSITIVE);
        } else {
            // Set them to be as fast as possible
            setSpeed(player, MAX_SPEED);
            player.sendMessage(Text.literal(ADJUSTING_SPEED_MSG), MessageTypes.POSITIVE);
        }

        return 1;
    }
}

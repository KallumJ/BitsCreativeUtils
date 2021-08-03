package team.bits.creative.utils;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;
import team.bits.creative.utils.builderswand.BuildersWandHandler;
import team.bits.creative.utils.commands.Commands;
import team.bits.creative.utils.listeners.PlayerConnectListener;
import team.bits.nibbles.event.misc.PlayerConnectEvent;
import team.bits.nibbles.event.misc.PlayerInteractWithBlockEvent;
import team.bits.nibbles.utils.Scheduler;

public class BitsCreativeUtils implements ModInitializer {
    private static FabricServerAudiences adventure;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> adventure = FabricServerAudiences.of(server));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> adventure = null);

        Commands.registerCommands();

        PlayerConnectEvent.EVENT.register(new PlayerConnectListener());

        BuildersWandHandler buildersWandHandler = new BuildersWandHandler();
        Scheduler.scheduleAtFixedRate(buildersWandHandler, 0, 5);
        PlayerInteractWithBlockEvent.EVENT.register(buildersWandHandler);
    }


    public static FabricServerAudiences adventure() {
        if (adventure == null) {
            throw new IllegalStateException("Tried to access Adventure without a running server!");
        }
        return adventure;
    }

    public static @NotNull Audience audience(@NotNull CommandOutput source) {
        return adventure().audience(source);
    }

    public static @NotNull Audience audience(@NotNull ServerCommandSource source) {
        return adventure().audience(source);
    }

}

package team.bits.creative.utils;

import net.fabricmc.api.ModInitializer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;
import team.bits.creative.utils.builderswand.BuildersWandHandler;
import team.bits.creative.utils.commands.Commands;
import team.bits.creative.utils.listeners.PlayerConnectListener;
import team.bits.nibbles.event.misc.ServerStartingEvent;
import team.bits.nibbles.event.misc.ServerStoppingEvent;
import team.bits.nibbles.utils.Scheduler;
import team.bits.nibbles.event.base.EventManager;

public class BitsCreativeUtils implements ModInitializer {
    private static FabricServerAudiences adventure;

    @Override
    public void onInitialize() {

        EventManager.INSTANCE.registerEvents((ServerStartingEvent.Listener) event -> adventure = FabricServerAudiences.of(event.getServer()));
        EventManager.INSTANCE.registerEvents((ServerStoppingEvent.Listener) event -> adventure = null);

        Commands.registerCommands();

        EventManager.INSTANCE.registerEvents(new PlayerConnectListener());
        BuildersWandHandler buildersWandHandler = new BuildersWandHandler();
        Scheduler.scheduleAtFixedRate(buildersWandHandler, 0, 5);

        EventManager.INSTANCE.registerEvents(buildersWandHandler);
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

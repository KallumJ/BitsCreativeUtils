package team.bits.creative.utils;

import net.fabricmc.api.ModInitializer;
import team.bits.creative.utils.builderswand.BuildersWandHandler;
import team.bits.creative.utils.commands.Commands;
import team.bits.creative.utils.listeners.PlayerConnectListener;
import team.bits.nibbles.event.base.EventManager;
import team.bits.nibbles.utils.Scheduler;

public class BitsCreativeUtils implements ModInitializer {
    @Override
    public void onInitialize() {
        Commands.registerCommands();

        EventManager.INSTANCE.registerEvents(new PlayerConnectListener());
        BuildersWandHandler buildersWandHandler = new BuildersWandHandler();
        Scheduler.scheduleAtFixedRate(buildersWandHandler, 0, 5);

        EventManager.INSTANCE.registerEvents(buildersWandHandler);
    }
}

package team.bits.creative.utils.listeners;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import team.bits.nibbles.event.misc.PlayerConnectEvent;
import team.bits.nibbles.utils.Scheduler;
import team.bits.nibbles.utils.ServerInstance;

public class PlayerConnectListener implements PlayerConnectEvent {

    @Override
    public void onPlayerConnect(@NotNull ServerPlayerEntity serverPlayerEntity, @NotNull ClientConnection clientConnection) {
        // see https://wiki.vg/Entity_statuses#Player
        final byte PERMISSION_LEVEL_2 = (byte) 26;

        Scheduler.schedule(() -> ServerInstance.get().getPlayerManager().sendToAll(
                new EntityStatusS2CPacket(serverPlayerEntity, PERMISSION_LEVEL_2)
        ), 40);
    }
}

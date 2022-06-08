package team.bits.creative.utils.listeners;

import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import team.bits.nibbles.event.server.PlayerConnectEvent;
import team.bits.nibbles.player.CopyPlayerDataEvent;
import team.bits.nibbles.utils.Scheduler;
import team.bits.nibbles.utils.ServerInstance;

public class PlayerConnectListener implements PlayerConnectEvent.Listener, CopyPlayerDataEvent.Listener {

    @Override
    public void onPlayerConnect(@NotNull PlayerConnectEvent playerConnectEvent) {
        sendPermissionUpdate(playerConnectEvent.getPlayer());
    }

    @Override
    public void onCopyPlayerData(@NotNull CopyPlayerDataEvent copyPlayerDataEvent) {
        sendPermissionUpdate((ServerPlayerEntity) copyPlayerDataEvent.getNewPlayer());
    }

    private static void sendPermissionUpdate(@NotNull ServerPlayerEntity player) {
        // see https://wiki.vg/Entity_statuses#Player
        final byte PERMISSION_LEVEL_2 = (byte) 26;
        Scheduler.schedule(() -> ServerInstance.get().getPlayerManager().sendToAll(
                new EntityStatusS2CPacket(player, PERMISSION_LEVEL_2)
        ), 40);
    }
}

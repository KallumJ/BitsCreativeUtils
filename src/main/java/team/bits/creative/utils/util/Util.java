package team.bits.creative.utils.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class Util {
    private static final double LOOK_DISTANCE = 4.5;

    public static TargettedBlock getTargetedBlock(ServerPlayerEntity player) {
        World world = player.getServerWorld();

        // Cast a ray from player with a max range of targetting distance
        BlockHitResult raycast = (BlockHitResult) player.raycast(LOOK_DISTANCE, 0f, false);

        return new TargettedBlock(world.getBlockState(raycast.getBlockPos()).getBlock(), raycast.getSide(), raycast.getBlockPos(), player);
    }

    public static boolean isBlockReplaceable(Block block) {
        return block.equals(Blocks.AIR) || block.equals(Blocks.CAVE_AIR) || block.equals(Blocks.VOID_AIR);
    }

}

package team.bits.creative.utils.util;

import net.minecraft.block.Block;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public record TargettedBlock(Block block, Direction side, BlockPos blockPos, ServerPlayerEntity player) {
}

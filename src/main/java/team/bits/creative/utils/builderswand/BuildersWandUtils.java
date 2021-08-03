package team.bits.creative.utils.builderswand;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;


public class BuildersWandUtils {

    public static final Item WAND_TYPE = Items.STICK;
    public static final int MAX_TARGET_DISTANCE = 5;
    public static final int LINE_WIDTH = 9;

    public static boolean isHoldingWand(@NotNull ServerPlayerEntity player) {
        return player.isHolding(WAND_TYPE);
    }

    public static @NotNull Collection<BlockPos> getTargetLocations(@NotNull ServerPlayerEntity player) {
        // get the block the player is looking at
        HitResult rayTraceResult = player.raycast(MAX_TARGET_DISTANCE, 0, false);
        // if the player isn't looking at a block, return nothing
        if (rayTraceResult.getType() == HitResult.Type.MISS || !(rayTraceResult instanceof BlockHitResult blockResult)) {
            return Collections.emptySet();
        }

        return getTargetLocations(player, blockResult);
    }

    public static @NotNull Collection<BlockPos> getTargetLocations(@NotNull ServerPlayerEntity player, @NotNull BlockHitResult blockResult) {
        ServerWorld world = player.getServerWorld();
        BlockPos directTarget = blockResult.getBlockPos();
        Direction directFace = blockResult.getSide();
        Direction oppositeFace = directFace.getOpposite();

        BlockPos origin = directTarget.add(directFace.getVector());
        Vec3i direction = getDirectionVector(player);
        Block type = world.getBlockState(directTarget).getBlock();

        // make a list for the target locations
        Collection<BlockPos> locations = new LinkedList<>();

        // if the origin block is replaceable, add it
        if (isReplaceable(world, origin)) {
            locations.add(origin);
        }

        // go left & right from the origin
        for (int i = 0; i < 2; i++) {
            // travel ((width - 1) / 2) blocks (aka half the width minus the original clicked block)
            for (int j = 0; j < ((LINE_WIDTH - 1) / 2); j++) {

                // get the location at that point
                BlockPos current = origin.add(direction.multiply(j + 1).multiply((i * 2) - 1));
                // get the block "below" it
                BlockPos block = current.add(oppositeFace.getVector());

                // if the type matches, and the target is replaceable, add it to the targets
                if (world.getBlockState(block).getBlock() == type && isReplaceable(world, current)) {
                    locations.add(current);
                } else {
                    // if it's not a match, break out of this branch
                    break;
                }
            }
        }

        return Collections.unmodifiableCollection(locations);
    }

    private static @NotNull Vec3i getDirectionVector(@NotNull ServerPlayerEntity player) {
        if (player.isSneaking()) {
            // if the player is sneaking we want vertical targeting
            return new Vec3i(0, 1, 0);
        } else {
            // otherwise, for NORTH and SOUTH directions we want X axis targeting
            // and for EAST and WEST directions we want Z axis targeting
            return switch (player.getHorizontalFacing()) {
                case NORTH, SOUTH -> new Vec3i(1, 0, 0);
                default -> new Vec3i(0, 0, 1);
            };
        }
    }

    private static boolean isReplaceable(@NotNull ServerWorld world, @NotNull BlockPos blockPos) {
        // check if the block is air
        return world.getBlockState(blockPos).isAir();
    }
}

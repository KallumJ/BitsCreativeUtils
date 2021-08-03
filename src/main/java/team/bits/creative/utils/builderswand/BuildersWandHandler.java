package team.bits.creative.utils.builderswand;

import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import team.bits.nibbles.event.misc.PlayerInteractWithBlockEvent;
import team.bits.nibbles.utils.ServerInstance;

import java.util.Collection;

import static team.bits.creative.utils.builderswand.BuildersWandUtils.MAX_TARGET_DISTANCE;

public class BuildersWandHandler implements Runnable, PlayerInteractWithBlockEvent {

    @Override
    public void run() {
        for (ServerPlayerEntity player : ServerInstance.getOnlinePlayers()) {
            // If the item in the players hand is the builders wand
            if (BuildersWandUtils.isHoldingWand(player)) {
                // show the targeting particles to the player
                showParticles(player);
            }
        }
    }

    @Override
    public @NotNull ActionResult onPlayerInteract(@NotNull ServerPlayerEntity player, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Hand hand) {
        if (BuildersWandUtils.isHoldingWand(player)) {

            // get the block the player is looking at
            HitResult rayTraceResult = player.raycast(MAX_TARGET_DISTANCE, 0, false);
            // if the player isn't looking at a block, do nothing
            if (rayTraceResult.getType() == HitResult.Type.MISS || !(rayTraceResult instanceof BlockHitResult blockResult)) {
                return ActionResult.PASS;
            }

            // the client can send multiple interact events, we only want the first one
            if (Util.getMeasuringTimeMs() - player.getLastActionTime() < 10) {
                return ActionResult.PASS;
            }

            final ServerWorld world = player.getServerWorld();
            final BlockPos origin = blockResult.getBlockPos();
            final Direction targetFace = blockResult.getSide();
            final Direction face = targetFace.getOpposite();

            // get all blocks targeted by the wand
            Collection<BlockPos> targets = BuildersWandUtils.getTargetLocations(player);

            // check if there are any targets
            if (!targets.isEmpty()) {

                // clone the original blocks to the new targets
                for (BlockPos target : targets) {
                    BlockState original = world.getBlockState(target.add(face.getVector()));
                    world.setBlockState(target, original);
                }

                // make the player swing their arm
                player.swingHand(hand);
                // play a block place sound
                world.playSound(
                        null, origin.getX(), origin.getY(), origin.getZ(),
                        SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f
                );

                return ActionResult.CONSUME;
            }
        }

        return ActionResult.PASS;
    }

    private static void showParticles(@NotNull ServerPlayerEntity player) {
        final ServerWorld world = player.getServerWorld();

        // get all blocks targeted by the wand
        BuildersWandUtils.getTargetLocations(player)
                // show a particle at the location
                .forEach(pos -> displayParticle(world, pos));
    }

    private static void displayParticle(@NotNull ServerWorld world, @NotNull BlockPos target) {
        // flame particle cuz it's nice and simple
        world.spawnParticles(
                ParticleTypes.FLAME,
                // add 0.5 to all coords to get the center of the block
                target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5,
                0, 0, 0, 0, 0
        );
    }
}

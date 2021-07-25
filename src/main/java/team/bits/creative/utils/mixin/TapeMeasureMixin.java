package team.bits.creative.utils.mixin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.bits.creative.utils.BitsCreativeUtils;

import java.util.HashMap;
import java.util.Map;

@Mixin(Item.class)
public class TapeMeasureMixin {
    private static final Map<PlayerEntity, BlockPos> previousLocations = new HashMap<>();
    private static final String MEASURING_MSG = "Measuring distance... Click another block to measure how far away it is!";
    private static final String DISTANCE_MSG = "Distance: ";
    private static final String X_STR = "X: %d, ";
    private static final String Y_STR = "Y: %d, ";
    private static final String Z_STR = "Z: %d";

    @Inject(at = @At("RETURN"), method = "useOnBlock")
    public void measureDistance(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();

        if (player != null && player.getStackInHand(hand).isOf(Items.BLAZE_ROD)) {
            BlockPos clickedBlockPos = context.getBlockPos();

            // If this player hasnt clicked before
            if (!previousLocations.containsKey(player)) {
                // Save the position they clicked, and inform them to click somewhere else
                previousLocations.put(player, clickedBlockPos);
                BitsCreativeUtils.audience(player).sendMessage(Component.text(MEASURING_MSG).color(NamedTextColor.YELLOW));
            } else {
                // If the player has clicked before, work out the distance between the 2 positions
                BlockPos previousBlockPos = previousLocations.get(player);

                Vec3i distanceVector = clickedBlockPos.subtract(previousBlockPos);

                int x = Math.abs(distanceVector.getX()) + 1;
                int y = Math.abs(distanceVector.getY()) + 1;
                int z = Math.abs(distanceVector.getZ()) + 1;

                BitsCreativeUtils.audience(player).sendMessage(Component.text().append(
                        Component.text(DISTANCE_MSG).color(NamedTextColor.YELLOW),
                        Component.text(String.format(X_STR, x)).color(NamedTextColor.RED),
                        Component.text(String.format(Y_STR, y)).color(NamedTextColor.GREEN),
                        Component.text(String.format(Z_STR, z)).color(NamedTextColor.AQUA)
                ));

                previousLocations.remove(player);
            }
        }
    }
}

package team.bits.creative.utils.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.bits.nibbles.utils.Colors;
import team.bits.nibbles.utils.MessageTypes;

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
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        Hand hand = context.getHand();

        if (player != null && player.getStackInHand(hand).isOf(Items.BLAZE_ROD)) {
            BlockPos clickedBlockPos = context.getBlockPos();

            // If this player hasnt clicked before
            if (!previousLocations.containsKey(player)) {
                // Save the position they clicked, and inform them to click somewhere else
                previousLocations.put(player, clickedBlockPos);

                player.sendMessage(Text.literal(MEASURING_MSG), MessageTypes.NEUTRAL);
            } else {
                // If the player has clicked before, work out the distance between the 2 positions
                BlockPos previousBlockPos = previousLocations.get(player);

                Vec3i distanceVector = clickedBlockPos.subtract(previousBlockPos);

                int x = Math.abs(distanceVector.getX()) + 1;
                int y = Math.abs(distanceVector.getY()) + 1;
                int z = Math.abs(distanceVector.getZ()) + 1;

                MutableText text = Text.literal(DISTANCE_MSG).styled(style -> style.withColor(Colors.NEUTRAL));
                text.append(Text.literal(String.format(X_STR, x)).styled(style -> style.withColor(TextColor.fromFormatting(Formatting.RED))));
                text.append(Text.literal(String.format(Y_STR, y)).styled(style -> style.withColor(TextColor.fromFormatting(Formatting.GREEN))));
                text.append(Text.literal(String.format(Z_STR, z)).styled(style -> style.withColor(TextColor.fromFormatting(Formatting.AQUA))));

                player.sendMessage(text, MessageTypes.PLAIN);

                previousLocations.remove(player);
            }
        }
    }
}

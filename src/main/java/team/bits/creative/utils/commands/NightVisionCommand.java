package team.bits.creative.utils.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import team.bits.creative.utils.BitsCreativeUtils;
import team.bits.nibbles.command.Command;
import team.bits.nibbles.command.CommandInformation;

public class NightVisionCommand extends Command {
    private static final String ADD_MSG = "Gave night vision";
    private static final String REMOVE_MSG = "Removed night vision";

    public NightVisionCommand() {
        super("nightvision", new CommandInformation()
            .setDescription("Toggles whether night vision is applied or not")
            .setPublic(true),
                "nv"
        );
    }

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (!player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            BitsCreativeUtils.audience(player).sendMessage(Component.text(ADD_MSG).color(NamedTextColor.GREEN));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false));
        } else {
            BitsCreativeUtils.audience(player).sendMessage(Component.text(REMOVE_MSG).color(NamedTextColor.GREEN));
            player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }

        return 1;
    }
}

package team.bits.creative.utils.mixin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.GameModeCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameModeCommand.class)
public class GameModeCommandMixin {
    @ModifyVariable(
            method = "register",
            at = @At("STORE"),
            ordinal = 0
    )
    private static LiteralArgumentBuilder<ServerCommandSource> allowExecution(LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder) {
        return CommandManager.literal("gamemode").requires((source) -> source.hasPermissionLevel(0));
    }
}

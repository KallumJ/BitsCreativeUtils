package team.bits.creative.utils.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerEntityInvoker {
    @Invoker("sendAbilitiesUpdate")
    void invokeSendAbilitiesUpdate();
}

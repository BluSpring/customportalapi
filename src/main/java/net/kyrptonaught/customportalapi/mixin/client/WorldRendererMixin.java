package net.kyrptonaught.customportalapi.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelEventHandler.class)
public class WorldRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @WrapOperation(method = "levelEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;"))
    public SoundEngine.PlayResult CPA$postTPSoundEvent(SoundManager instance, SoundInstance sound, Operation<SoundEngine.PlayResult> original, @Local(ordinal = 0, argsOnly = true) int eventId, @Local(ordinal = 1, argsOnly = true) int data) {
        if (eventId == 1032 && data != 0) {
            Block block = BuiltInRegistries.BLOCK.byId(data);
            PortalLink link = CustomPortalApiRegistry.getPortalLinkFromBase(block);
            if (link != null && link.getPostTpPortalAmbienceEvent().hasEvent()) {
                return original.call(instance, link.getPostTpPortalAmbienceEvent().execute(minecraft.player).getInstance());
            }
        }
        return original.call(instance, sound);
    }
}

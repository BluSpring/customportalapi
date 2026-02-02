package net.kyrptonaught.customportalapi.mixin.client;

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
    private Minecraft client;

    @Redirect(method = "processWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;)Lnet/minecraft/client/sound/SoundSystem$PlayResult;"))
    public SoundEngine.PlayResult CPA$postTPSoundEvent(SoundManager instance, SoundInstance sound, int eventId, BlockPos pos, int data) {
        if (eventId == 1032 && data != 0) {
            Block block = BuiltInRegistries.BLOCK.byId(data);
            PortalLink link = CustomPortalApiRegistry.getPortalLinkFromBase(block);
            if (link != null && link.getPostTpPortalAmbienceEvent().hasEvent()) {
                instance.play(link.getPostTpPortalAmbienceEvent().execute(client.player).getInstance());
                return null;
            }
        }
        instance.play(sound);
        return null;
    }
}

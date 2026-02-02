package net.kyrptonaught.customportalapi.mixin.client;

import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PortalProcessor;
import net.minecraft.world.level.block.Portal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class ClientPlayerEntityMixin {

    @Redirect(method = "tickNausea", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;)Lnet/minecraft/client/sound/SoundSystem$PlayResult;"))
    public SoundEngine.PlayResult playCustomPortalAmbiance(SoundManager instance, SoundInstance sound) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        PortalLink link = isCustomPortal(player);
        if (link != null && link.getInPortalAmbienceEvent().hasEvent()) {
            instance.play(link.getInPortalAmbienceEvent().execute(player).getInstance());
            return null;
        }
        instance.play(sound);
        return null;
    }

    @Unique
    private PortalLink isCustomPortal(LocalPlayer player) {
        PortalProcessor portalManager = player.portalProcess;
        Portal portalBlock = portalManager != null && portalManager.isInsidePortalThisTick() ? ((PortalManagerAccessor) portalManager).getPortal() : null;
        BlockPos portalPos = portalManager != null && portalManager.isInsidePortalThisTick() ? ((PortalManagerAccessor) portalManager).getPos() : null;

        if (portalBlock instanceof CustomPortalBlock)
            return CustomPortalApiRegistry.getPortalLinkFromBase(((CustomPortalBlock) portalBlock).getPortalBase(player.level(), portalPos));

        return null;
    }
}

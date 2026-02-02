package net.kyrptonaught.customportalapi.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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

    @WrapOperation(method = "handlePortalTransitionEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;"))
    public SoundEngine.PlayResult playCustomPortalAmbiance(SoundManager instance, SoundInstance sound, Operation<SoundEngine.PlayResult> original) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        PortalLink link = isCustomPortal(player);
        if (link != null && link.getInPortalAmbienceEvent().hasEvent()) {
            return original.call(instance, link.getInPortalAmbienceEvent().execute(player).getInstance());
        }
        return original.call(instance, sound);
    }

    @Unique
    private PortalLink isCustomPortal(LocalPlayer player) {
        PortalProcessor portalManager = player.portalProcess;
        Portal portalBlock = portalManager != null && portalManager.isInsidePortalThisTick() ? ((PortalManagerAccessor) portalManager).getPortal() : null;
        BlockPos portalPos = portalManager != null && portalManager.isInsidePortalThisTick() ? ((PortalManagerAccessor) portalManager).getEntryPosition() : null;

        if (portalBlock instanceof CustomPortalBlock)
            return CustomPortalApiRegistry.getPortalLinkFromBase(((CustomPortalBlock) portalBlock).getPortalBase(player.level(), portalPos));

        return null;
    }
}

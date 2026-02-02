package net.kyrptonaught.customportalapi.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.PortalProcessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public class InGameHudMixin {

    @Shadow
    @Final
    private Minecraft client;

    @Unique
    private int lastColor = -1;

    @Redirect(method = "renderPortalOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/ColorHelper;getWhite(F)I", ordinal = 0))
    public int changeColor(float alpha) {
        isCustomPortal(client.player);
        if (lastColor >= 0)
            return ARGB.color(ARGB.as8BitChannel(alpha), lastColor);
        return ARGB.white(alpha);
    }

    @Redirect(method = "renderPortalOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockModels;getModelParticleSprite(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/texture/Sprite;"))
    public TextureAtlasSprite renderCustomPortalOverlay(BlockModelShaper blockModels, BlockState blockState) {
        if (lastColor >= 0) {
            return this.client.getBlockRenderer().getBlockModelShaper().getParticleIcon(CustomPortalsMod.portalBlock.defaultBlockState());
        }
        return this.client.getBlockRenderer().getBlockModelShaper().getParticleIcon(Blocks.NETHER_PORTAL.defaultBlockState());
    }


    @Unique
    private void isCustomPortal(LocalPlayer player) {
        PortalProcessor portalManager = player.portalProcess;
        Portal portalBlock = portalManager != null && portalManager.isInsidePortalThisTick() ? ((PortalManagerAccessor) portalManager).getPortal() : null;
        BlockPos portalPos = portalManager != null && portalManager.isInsidePortalThisTick() ? ((PortalManagerAccessor) portalManager).getPos() : null;

        if (portalBlock == null) {
            return;
        }

        if (portalBlock instanceof CustomPortalBlock) {
            PortalLink link = CustomPortalApiRegistry.getPortalLinkFromBase(((CustomPortalBlock) portalBlock).getPortalBase(player.level(), portalPos));
            if (link != null) {
                lastColor = link.colorID;
                return;
            }
        }

        lastColor = -1;
    }
}

package net.kyrptonaught.customportalapi.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ChunkSectionLayerMap;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.kyrptonaught.customportalapi.PerWorldPortals;
import net.kyrptonaught.customportalapi.networking.NetworkManager;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

@Environment(EnvType.CLIENT)
public class CustomPortalsModClient implements ClientModInitializer {

    public static ParticleType<BlockParticleOption> CUSTOMPORTALPARTICLE = FabricParticleTypes.complex(BlockParticleOption::codec, BlockParticleOption::streamCodec);

    @Override
    public void onInitializeClient() {

        ChunkSectionLayerMap.putBlock(CustomPortalsMod.portalBlock, ChunkSectionLayer.TRANSLUCENT);
        BlockColorRegistry.register((state, world, pos, tintIndex) -> {
            if (pos != null) {
                Block block = CustomPortalHelper.getPortalBase(Minecraft.getInstance().level, pos.immutable());
                PortalLink link = CustomPortalApiRegistry.getPortalLinkFromBase(block);
                if (link != null) return link.colorID;
            }
            return 1908001;
        }, CustomPortalsMod.portalBlock);

        Registry.register(BuiltInRegistries.PARTICLE_TYPE, CustomPortalsMod.MOD_ID + ":customportalparticle", CUSTOMPORTALPARTICLE);
        ParticleProviderRegistry.getInstance().register(CUSTOMPORTALPARTICLE, CustomPortalParticle.Factory::new);

        NetworkManager.registerPackets();
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            PerWorldPortals.removeOldPortalsFromRegistry();
        });
    }
}
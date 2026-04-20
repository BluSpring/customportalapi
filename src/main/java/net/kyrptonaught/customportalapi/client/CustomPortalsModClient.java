package net.kyrptonaught.customportalapi.client;

import java.util.List;

import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.kyrptonaught.customportalapi.PerWorldPortals;
import net.kyrptonaught.customportalapi.networking.NetworkManager;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.kyrptonaught.customportalapi.util.PortalLink;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;

@Environment(EnvType.CLIENT)
public class CustomPortalsModClient implements ClientModInitializer {

    public static ParticleType<BlockParticleOption> CUSTOMPORTALPARTICLE = FabricParticleTypes.complex(BlockParticleOption::codec, BlockParticleOption::streamCodec);

    @Override
    public void onInitializeClient() {
        BlockColorRegistry.register(List.of(new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return 1908001;
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                Block block = CustomPortalHelper.getPortalBase(Minecraft.getInstance().level, pos.immutable());
                PortalLink link = CustomPortalApiRegistry.getPortalLinkFromBase(block);
                if (link != null)
                    return link.colorID;

                return color(state);
            }
        }), CustomPortalsMod.portalBlock);

        Registry.register(BuiltInRegistries.PARTICLE_TYPE, CustomPortalsMod.MOD_ID + ":customportalparticle", CUSTOMPORTALPARTICLE);
        ParticleProviderRegistry.getInstance().register(CUSTOMPORTALPARTICLE, CustomPortalParticle.Factory::new);

        NetworkManager.registerPackets();
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            PerWorldPortals.removeOldPortalsFromRegistry();
        });
    }
}
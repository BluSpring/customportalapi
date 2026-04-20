package net.kyrptonaught.customportalapi;

import net.kyrptonaught.customportalapi.networking.ForcePlacePacket;
import net.kyrptonaught.customportalapi.networking.LinkSyncPacket;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.kyrptonaught.customportalapi.portal.PortalPlacer;
import net.kyrptonaught.customportalapi.portal.frame.FlatPortalAreaHelper;
import net.kyrptonaught.customportalapi.portal.frame.VanillaPortalAreaHelper;
import net.kyrptonaught.customportalapi.portal.linking.PortalLinkingStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.SavedDataStorage;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;


public class CustomPortalsMod implements ModInitializer {
    public static final String MOD_ID = "customportalapi";
    public static CustomPortalBlock portalBlock;
    public static Identifier VANILLAPORTAL_FRAMETESTER = Identifier.fromNamespaceAndPath(MOD_ID, "vanillanether");
    public static Identifier FLATPORTAL_FRAMETESTER = Identifier.fromNamespaceAndPath(MOD_ID, "flat");
    public static PortalLinkingStorage portalLinkingStorage;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            SavedDataStorage persistentStateManager = server.getLevel(Level.OVERWORLD).getDataStorage();
            portalLinkingStorage = persistentStateManager.computeIfAbsent(PortalLinkingStorage.getPersistentStateType());
        });
        CustomPortalApiRegistry.registerPortalFrameTester(VANILLAPORTAL_FRAMETESTER, VanillaPortalAreaHelper::new);
        CustomPortalApiRegistry.registerPortalFrameTester(FLATPORTAL_FRAMETESTER, FlatPortalAreaHelper::new);
        UseItemCallback.EVENT.register(((player, world, hand) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (!world.isClientSide()) {
                Item item = stack.getItem();
                if (PortalIgnitionSource.isRegisteredIgnitionSourceWith(item)) {
                    HitResult hit = player.pick(6, 1, false);
                    if (hit.getType() == HitResult.Type.BLOCK) {
                        BlockHitResult blockHit = (BlockHitResult) hit;
                        BlockPos usedBlockPos = blockHit.getBlockPos();
                        if (PortalPlacer.attemptPortalLight(world, usedBlockPos.relative(blockHit.getDirection()), PortalIgnitionSource.ItemUseSource(item).withPlayer(player))) {
                            return InteractionResult.SUCCESS_SERVER;
                        }
                    }
                }
            }
            return InteractionResult.PASS;
        }));

        PayloadTypeRegistry.clientboundPlay().register(LinkSyncPacket.PACKET_ID, LinkSyncPacket.codec);
        PayloadTypeRegistry.clientboundPlay().register(ForcePlacePacket.PACKET_ID, ForcePlacePacket.codec);

        /*
        CustomPortalBuilder.beginPortal().frameBlock(Blocks.GLOWSTONE).destDimID(Identifier.of("the_nether")).lightWithWater().setPortalSearchYRange(126, 256).tintColor(125, 20, 20).registerPortal();
        CustomPortalBuilder.beginPortal().frameBlock(Blocks.OBSIDIAN).destDimID(Identifier.of("the_end")).tintColor(66, 135, 245).registerPortalForced();
        CustomPortalBuilder.beginPortal().frameBlock(Blocks.COBBLESTONE).lightWithItem(Items.STICK).destDimID(Identifier.of("the_end")).tintColor(45, 24, 45).flatPortal().registerPortal();


        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.EMERALD_BLOCK)
                .lightWithWater()
                .destDimID(Identifier.of("the_end"))
                .tintColor(25, 76, 156)
                .flatPortal()
                .registerInPortalAmbienceSound(player -> new CPASoundEventData(SoundEvents.BLOCK_ANVIL_LAND, player.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F))
                .registerPostTPPortalAmbience(player -> new CPASoundEventData(SoundEvents.BLOCK_ANVIL_LAND, player.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F))
                .registerPortal();
*/
    }

    public static void logError(String message) {
        System.out.println("[" + MOD_ID + "]ERROR: " + message);
    }

    public static Block getDefaultPortalBlock() {
        return portalBlock;
    }

    // to guarantee block exists before use, unsure how safe this is but works for now. Don't want to switch to using a custom entrypoint to break compatibility with existing mods just yet
    //todo fix this with CustomPortalBuilder?
    static {
        portalBlock = (CustomPortalBlock) Blocks.register(
                ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CustomPortalsMod.MOD_ID, "customportalblock")),
                CustomPortalBlock::new,
                BlockBehaviour.Properties.of()
                        .noCollision()
                        .randomTicks()
                        .strength(-1.0f)
                        .sound(SoundType.GLASS)
                        .lightLevel(state -> 11)
                        .pushReaction(PushReaction.BLOCK)
        );

    }
}

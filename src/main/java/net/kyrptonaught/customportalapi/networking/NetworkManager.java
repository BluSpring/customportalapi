package net.kyrptonaught.customportalapi.networking;


import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.kyrptonaught.customportalapi.PerWorldPortals;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class NetworkManager implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        ServerPlayConnectionEvents.JOIN.register((serverPlayNetworkHandler, packetSender, minecraftServer) -> {
            for (PortalLink link : CustomPortalApiRegistry.getAllPortalLinks()) {
                packetSender.sendPacket(createPacket(link));
            }
        });
    }

    public static void syncLinkToAllPlayers(PortalLink link, MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            syncLinkToPlayer(link, player);
        }
    }

    public static void syncLinkToPlayer(PortalLink link, ServerPlayer player) {
        ServerPlayNetworking.send(player, createPacket(link));
    }

    private static LinkSyncPacket createPacket(PortalLink link) {
        return new LinkSyncPacket(link.block, link.dimID, link.colorID);
    }

    public static void sendForcePacket(ServerPlayer player, BlockPos pos, Direction.Axis axis) {
        ServerPlayNetworking.send(player, new ForcePlacePacket(pos, axis.ordinal()));
    }

    @Environment(EnvType.CLIENT)
    public static void registerPackets() {
        ClientPlayNetworking.registerGlobalReceiver(LinkSyncPacket.PACKET_ID, (payload, context) -> {
            PerWorldPortals.registerWorldPortal(new PortalLink(payload.blockID(), payload.dimID(), payload.color()));
        });

        ClientPlayNetworking.registerGlobalReceiver(ForcePlacePacket.PACKET_ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().level == null) return;

                Direction.Axis axis;
                if (payload.axis() > -1) {
                    axis = Direction.Axis.values()[payload.axis()];
                } else {
                    BlockState old = context.client().level.getBlockState(payload.pos());
                    axis = CustomPortalHelper.getAxisFrom(old);
                }

                context.client().level.setBlockAndUpdate(payload.pos(), CustomPortalHelper.blockWithAxis(CustomPortalsMod.getDefaultPortalBlock().defaultBlockState(), axis));

            });
        });
    }
}
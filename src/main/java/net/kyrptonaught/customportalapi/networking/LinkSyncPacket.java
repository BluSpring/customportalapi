package net.kyrptonaught.customportalapi.networking;

import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record LinkSyncPacket(ResourceLocation blockID, ResourceLocation dimID, int color) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LinkSyncPacket> PACKET_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CustomPortalsMod.MOD_ID, "syncportals"));
    public static final StreamCodec<RegistryFriendlyByteBuf, LinkSyncPacket> codec = StreamCodec.ofMember(LinkSyncPacket::write, LinkSyncPacket::read);

    public static LinkSyncPacket read(RegistryFriendlyByteBuf buf) {
        return new LinkSyncPacket(buf.readResourceLocation(), buf.readResourceLocation(), buf.readInt());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeResourceLocation(blockID);
        buf.writeResourceLocation(dimID);
        buf.writeInt(color);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
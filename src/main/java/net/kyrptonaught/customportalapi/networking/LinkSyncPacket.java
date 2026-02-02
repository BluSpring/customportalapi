package net.kyrptonaught.customportalapi.networking;

import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record LinkSyncPacket(Identifier blockID, Identifier dimID, int color) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LinkSyncPacket> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(CustomPortalsMod.MOD_ID, "syncportals"));
    public static final StreamCodec<RegistryFriendlyByteBuf, LinkSyncPacket> codec = StreamCodec.ofMember(LinkSyncPacket::write, LinkSyncPacket::read);

    public static LinkSyncPacket read(RegistryFriendlyByteBuf buf) {
        return new LinkSyncPacket(buf.readIdentifier(), buf.readIdentifier(), buf.readInt());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeIdentifier(blockID);
        buf.writeIdentifier(dimID);
        buf.writeInt(color);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
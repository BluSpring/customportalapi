package net.kyrptonaught.customportalapi.networking;

import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ForcePlacePacket(BlockPos pos, int axis) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ForcePlacePacket> PACKET_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CustomPortalsMod.MOD_ID, "forceplace"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ForcePlacePacket> codec = StreamCodec.ofMember(ForcePlacePacket::write, ForcePlacePacket::read);

    public static ForcePlacePacket read(RegistryFriendlyByteBuf buf) {
        return new ForcePlacePacket(buf.readBlockPos(), buf.readInt());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(axis);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
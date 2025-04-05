package net.kyrptonaught.customportalapi.portal.linking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PortalLinkingStorage extends PersistentState {
    public static final Codec<PortalLinkingStorage> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Entry.CODEC.listOf().fieldOf("portalLinks").forGetter(PortalLinkingStorage::getEntries)
            ).apply(instance, PortalLinkingStorage::new));

    private final ConcurrentHashMap<Identifier, ConcurrentHashMap<BlockPos, DimensionalBlockPos>> portalLinks = new ConcurrentHashMap<>();

    public PortalLinkingStorage() {

    }

    public PortalLinkingStorage(List<Entry> entries) {
        for (Entry entry : entries) {
            addLink(BlockPos.fromLong(entry.fromPos()), entry.fromID, entry.to().pos, entry.to().getDimension());
        }
    }

    public static PersistentStateType<PortalLinkingStorage> getPersistentStateType() {
        return new PersistentStateType<>(CustomPortalsMod.MOD_ID, PortalLinkingStorage::new, CODEC, DataFixTypes.LEVEL);
    }

    public List<Entry> getEntries() {
        List<Entry> entries = new ArrayList<>();

        portalLinks.keys().asIterator().forEachRemaining(dimKey -> {
            portalLinks.get(dimKey).forEach((blockPos, dimensionalBlockPos) -> {
                entries.add(new Entry(dimKey, blockPos.asLong(), dimensionalBlockPos));
            });
        });

        return entries;
    }

    public DimensionalBlockPos getDestination(BlockPos portalFramePos, RegistryKey<World> dimID) {
        if (dimID != null && portalFramePos != null && portalLinks.containsKey(dimID.getValue()))
            return portalLinks.get(dimID.getValue()).get(portalFramePos);

        return null;
    }

    public void createLink(BlockPos portalFramePos, RegistryKey<World> dimID, BlockPos destPortalFramePos, RegistryKey<World> destDimID) {
        addLink(portalFramePos, dimID, destPortalFramePos, destDimID);
        addLink(destPortalFramePos, destDimID, portalFramePos, dimID);
    }

    private void addLink(BlockPos portalFramePos, Identifier dimID, BlockPos destPortalFramePos, Identifier destDimID) {
        if (!portalLinks.containsKey(dimID))
            portalLinks.put(dimID, new ConcurrentHashMap<>());
        portalLinks.get(dimID).put(portalFramePos, new DimensionalBlockPos(destDimID, destPortalFramePos));
    }

    private void addLink(BlockPos portalFramePos, RegistryKey<World> dimID, BlockPos destPortalFramePos, RegistryKey<World> destDimID) {
        addLink(portalFramePos, dimID.getValue(), destPortalFramePos, destDimID.getValue());
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    public record Entry(Identifier fromID, Long fromPos, DimensionalBlockPos to) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Identifier.CODEC.fieldOf("fromDimID").forGetter(Entry::fromID),
                        Codec.LONG.fieldOf("fromPos").forGetter(Entry::fromPos),
                        DimensionalBlockPos.CODEC.fieldOf("to").forGetter(Entry::to)
                ).apply(instance, Entry::new));
    }
}
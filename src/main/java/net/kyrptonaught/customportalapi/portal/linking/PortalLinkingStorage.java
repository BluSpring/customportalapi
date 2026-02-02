package net.kyrptonaught.customportalapi.portal.linking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PortalLinkingStorage extends SavedData {
    public static final Codec<PortalLinkingStorage> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Entry.CODEC.listOf().fieldOf("portalLinks").forGetter(PortalLinkingStorage::getEntries)
            ).apply(instance, PortalLinkingStorage::new));

    private final ConcurrentHashMap<ResourceLocation, ConcurrentHashMap<BlockPos, DimensionalBlockPos>> portalLinks = new ConcurrentHashMap<>();

    public PortalLinkingStorage() {

    }

    public PortalLinkingStorage(List<Entry> entries) {
        for (Entry entry : entries) {
            addLink(BlockPos.of(entry.fromPos()), entry.fromID, entry.to().pos, entry.to().getDimension());
        }
    }

    public static SavedDataType<PortalLinkingStorage> getPersistentStateType() {
        return new SavedDataType<>(CustomPortalsMod.MOD_ID, PortalLinkingStorage::new, CODEC, DataFixTypes.LEVEL);
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

    public DimensionalBlockPos getDestination(BlockPos portalFramePos, ResourceKey<Level> dimID) {
        if (dimID != null && portalFramePos != null && portalLinks.containsKey(dimID.location()))
            return portalLinks.get(dimID.location()).get(portalFramePos);

        return null;
    }

    public void createLink(BlockPos portalFramePos, ResourceKey<Level> dimID, BlockPos destPortalFramePos, ResourceKey<Level> destDimID) {
        addLink(portalFramePos, dimID, destPortalFramePos, destDimID);
        addLink(destPortalFramePos, destDimID, portalFramePos, dimID);
    }

    private void addLink(BlockPos portalFramePos, ResourceLocation dimID, BlockPos destPortalFramePos, ResourceLocation destDimID) {
        if (!portalLinks.containsKey(dimID))
            portalLinks.put(dimID, new ConcurrentHashMap<>());
        portalLinks.get(dimID).put(portalFramePos, new DimensionalBlockPos(destDimID, destPortalFramePos));
    }

    private void addLink(BlockPos portalFramePos, ResourceKey<Level> dimID, BlockPos destPortalFramePos, ResourceKey<Level> destDimID) {
        addLink(portalFramePos, dimID.location(), destPortalFramePos, destDimID.location());
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    public record Entry(ResourceLocation fromID, Long fromPos, DimensionalBlockPos to) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        ResourceLocation.CODEC.fieldOf("fromDimID").forGetter(Entry::fromID),
                        Codec.LONG.fieldOf("fromPos").forGetter(Entry::fromPos),
                        DimensionalBlockPos.CODEC.fieldOf("to").forGetter(Entry::to)
                ).apply(instance, Entry::new));
    }
}
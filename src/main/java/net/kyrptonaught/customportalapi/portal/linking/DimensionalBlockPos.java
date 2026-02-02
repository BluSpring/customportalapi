package net.kyrptonaught.customportalapi.portal.linking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class DimensionalBlockPos {
    public static final Codec<DimensionalBlockPos> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("dimID").forGetter(DimensionalBlockPos::getDimension),
                    Codec.LONG.fieldOf("pos").forGetter(DimensionalBlockPos::getPosLong)
            ).apply(instance, DimensionalBlockPos::new));

    public ResourceLocation dimensionType;
    public BlockPos pos;

    public DimensionalBlockPos(ResourceLocation dimension, BlockPos pos) {
        this.pos = pos;
        this.dimensionType = dimension;
    }

    public DimensionalBlockPos(ResourceLocation dimension, Long pos) {
        this(dimension, BlockPos.of(pos));
    }

    public ResourceLocation getDimension() {
        return dimensionType;
    }

    public Long getPosLong() {
        return pos.asLong();
    }
}
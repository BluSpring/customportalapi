package net.kyrptonaught.customportalapi.portal.linking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

public class DimensionalBlockPos {
    public static final Codec<DimensionalBlockPos> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Identifier.CODEC.fieldOf("dimID").forGetter(DimensionalBlockPos::getDimension),
                    Codec.LONG.fieldOf("pos").forGetter(DimensionalBlockPos::getPosLong)
            ).apply(instance, DimensionalBlockPos::new));

    public Identifier dimensionType;
    public BlockPos pos;

    public DimensionalBlockPos(Identifier dimension, BlockPos pos) {
        this.pos = pos;
        this.dimensionType = dimension;
    }

    public DimensionalBlockPos(Identifier dimension, Long pos) {
        this(dimension, BlockPos.of(pos));
    }

    public Identifier getDimension() {
        return dimensionType;
    }

    public Long getPosLong() {
        return pos.asLong();
    }
}
package net.kyrptonaught.customportalapi.event;

import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface PortalPreIgniteEvent {
    boolean attemptLight(Player player, Level world, BlockPos portalPos, BlockPos framePos, PortalIgnitionSource portalIgnitionSource);
}

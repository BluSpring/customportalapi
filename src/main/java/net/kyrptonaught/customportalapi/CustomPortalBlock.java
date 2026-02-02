package net.kyrptonaught.customportalapi;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.customportalapi.client.CustomPortalsModClient;
import net.kyrptonaught.customportalapi.portal.frame.PortalFrameTester;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.kyrptonaught.customportalapi.util.CustomTeleporter;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CustomPortalBlock extends Block implements Portal {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    protected static final VoxelShape X_SHAPE = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
    protected static final VoxelShape Z_SHAPE = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);
    protected static final VoxelShape Y_SHAPE = Block.box(0.0D, 6.0D, 0.0D, 16.0D, 10.0D, 16.0D);

    public CustomPortalBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(AXIS)) {
            case Z -> Z_SHAPE;
            case Y -> Y_SHAPE;
            default -> X_SHAPE;
        };
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        Block block = getPortalBase((Level) world, pos);
        PortalLink link = CustomPortalApiRegistry.getPortalLinkFromBase(block);
        if (link != null) {
            PortalFrameTester portalFrameTester = link.getFrameTester().createInstanceOfPortalFrameTester().init((LevelAccessor) world, pos, CustomPortalHelper.getAxisFrom(state), block);
            if (portalFrameTester.isAlreadyLitPortalFrame())
                return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
        }
        //todo handle unknown portallink

        return Blocks.AIR.defaultBlockState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
        return ItemStack.EMPTY;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(100) == 0) {
            world.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
        }

        for (int i = 0; i < 4; ++i) {
            double d = (double) pos.getX() + random.nextDouble();
            double e = (double) pos.getY() + random.nextDouble();
            double f = (double) pos.getZ() + random.nextDouble();
            double g = ((double) random.nextFloat() - 0.5) * 0.5;
            double h = ((double) random.nextFloat() - 0.5) * 0.5;
            double j = ((double) random.nextFloat() - 0.5) * 0.5;
            int k = random.nextInt(2) * 2 - 1;

            if (state.getValue(AXIS) == Direction.Axis.Y) {
                h = random.nextFloat() * 2.0f * (float) k;
            } else {
                if (world.getBlockState(pos.west()).is(this) || world.getBlockState(pos.east()).is(this)) {
                    f = (double) pos.getZ() + 0.5 + 0.25 * (double) k;
                    j = random.nextFloat() * 2.0f * (float) k;
                } else {
                    d = (double) pos.getX() + 0.5 + 0.25 * (double) k;
                    g = random.nextFloat() * 2.0f * (float) k;
                }
            }
            world.addParticle(new BlockParticleOption(CustomPortalsModClient.CUSTOMPORTALPARTICLE, getPortalBase(world, pos).defaultBlockState()), d, e, f, g, h, j);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler) {
        if (entity.canUsePortal(false)) {
            entity.setAsInsidePortal(this, pos);
        }
    }

    public Block getPortalBase(Level world, BlockPos pos) {
        return CustomPortalHelper.getPortalBaseDefault(world, pos);
    }

    @Override
    public int getPortalTransitionTime(ServerLevel world, Entity entity) {
        if (entity instanceof Player playerEntity) {
            return Math.max(1, world.getGameRules().getInt(playerEntity.getAbilities().invulnerable ? GameRules.RULE_PLAYERS_NETHER_PORTAL_CREATIVE_DELAY : GameRules.RULE_PLAYERS_NETHER_PORTAL_DEFAULT_DELAY));
        } else {
            return 0;
        }
    }

    @Override
    public @Nullable TeleportTransition getPortalDestination(ServerLevel world, Entity entity, BlockPos pos) {
        return CustomTeleporter.createTeleportTarget(world, entity, getPortalBase(world, pos), pos);
    }

    @Override
    public Transition getLocalTransition() {
        return Transition.CONFUSION;
    }
}
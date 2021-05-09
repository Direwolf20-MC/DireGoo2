package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.common.tiles.GooliminationFieldGenTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class GooliminationFieldGen extends Block {
    protected static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(11, 10, 0, 16, 11, 5),
            Block.makeCuboidShape(0, 0, 0, 16, 10, 16),
            Block.makeCuboidShape(6, 11, 6, 10, 16, 10),
            Block.makeCuboidShape(6, 11, 11.5, 10, 12, 15.5),
            Block.makeCuboidShape(6, 11, 0.5, 10, 12, 4.5),
            Block.makeCuboidShape(0.5, 11, 6, 4.5, 14, 10),
            Block.makeCuboidShape(0.5, 11, 11.5, 4.5, 12, 15.5),
            Block.makeCuboidShape(0.5, 11, 0.5, 4.5, 12, 4.5),
            Block.makeCuboidShape(2, 12, 2, 3, 13, 3),
            Block.makeCuboidShape(2, 12, 13, 3, 13, 14),
            Block.makeCuboidShape(7.5, 12, 2, 8.5, 13, 3),
            Block.makeCuboidShape(7.5, 12, 13, 8.5, 13, 14),
            Block.makeCuboidShape(13, 12, 2, 14, 13, 3),
            Block.makeCuboidShape(13, 12, 13, 14, 13, 14),
            Block.makeCuboidShape(2, 13, 10, 3, 14, 14),
            Block.makeCuboidShape(2, 13, 2, 3, 14, 6),
            Block.makeCuboidShape(7.5, 13, 10, 8.5, 14, 14),
            Block.makeCuboidShape(7.5, 13, 2, 8.5, 14, 6),
            Block.makeCuboidShape(13, 13, 10, 14, 14, 14),
            Block.makeCuboidShape(13, 13, 2, 14, 14, 6),
            Block.makeCuboidShape(11.5, 11, 6, 15.5, 14, 10),
            Block.makeCuboidShape(11.5, 11, 11.5, 15.5, 12, 15.5),
            Block.makeCuboidShape(11.5, 11, 0.5, 15.5, 12, 4.5),
            Block.makeCuboidShape(5.5, 10, 5.5, 10.5, 11, 10.5),
            Block.makeCuboidShape(5.5, 10, 11, 10.5, 11, 16),
            Block.makeCuboidShape(5.5, 10, 0, 10.5, 11, 5),
            Block.makeCuboidShape(0, 10, 5.5, 5, 11, 10.5),
            Block.makeCuboidShape(0, 10, 11, 5, 11, 16),
            Block.makeCuboidShape(0, 10, 0, 5, 11, 5),
            Block.makeCuboidShape(11, 10, 5.5, 16, 11, 10.5),
            Block.makeCuboidShape(11, 10, 11, 16, 11, 16)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    public GooliminationFieldGen() {
        super(
                Properties.create(Material.IRON)
                        .hardnessAndResistance(2.0f)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("tooltip.diregoo.gooliminationblock").mergeStyle(TextFormatting.GREEN));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new GooliminationFieldGenTile();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        // Only execute on the server
        if (worldIn.isRemote)
            return ActionResultType.SUCCESS;

        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof GooliminationFieldGenTile))
            return ActionResultType.FAIL;

        /*if (player.isSneaking()) {
            if (((GooliminationFieldGenTile) te).isActive())
                ((GooliminationFieldGenTile) te).deactivate((ServerWorld) worldIn);
            else
                ((GooliminationFieldGenTile) te).activate((ServerWorld) worldIn);
        } else {*/
        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, pos);
        //}
        return ActionResultType.SUCCESS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!worldIn.isRemote) {
            if (newState.getBlock() != this) {
                TileEntity tileEntity = worldIn.getTileEntity(pos);
                if (tileEntity != null) {
                    if (tileEntity instanceof GooliminationFieldGenTile) {
                        ((GooliminationFieldGenTile) tileEntity).deactivate((ServerWorld) worldIn);
                    }
                }
                super.onReplaced(state, worldIn, pos, newState, isMoving);
            }
        }
    }
}

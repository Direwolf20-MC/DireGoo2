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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class GooliminationFieldGen extends Block {
    public GooliminationFieldGen() {
        super(
                Properties.create(Material.IRON)
                        .hardnessAndResistance(2.0f)
        );
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
}

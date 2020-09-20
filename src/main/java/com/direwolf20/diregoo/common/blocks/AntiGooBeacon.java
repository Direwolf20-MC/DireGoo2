package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.common.tiles.AntiGooBeaconTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class AntiGooBeacon extends Block {
    public AntiGooBeacon() {
        super(
                Properties.create(Material.IRON)
                        .hardnessAndResistance(2.0f)
        );
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AntiGooBeaconTileEntity();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (newState.getBlock() != this) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity != null) {
                if (tileEntity instanceof AntiGooBeaconTileEntity) {
                    ((AntiGooBeaconTileEntity) tileEntity).removeField();
                    dropInventoryItems(worldIn, pos, ((AntiGooBeaconTileEntity) tileEntity).getInventoryStacks());
                    worldIn.updateComparatorOutputLevel(pos, this);
                }
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        // Client Only
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        }

        //Server only
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (!(te instanceof AntiGooBeaconTileEntity))
                return ActionResultType.FAIL;

            if (player.isSneaking()) {
                /*if (((AntiGooBeaconTileEntity) te).isActive())
                    ((AntiGooBeaconTileEntity) te).removeField();
                else
                    ((AntiGooBeaconTileEntity) te).addField();*/
                return ActionResultType.SUCCESS;
            } else {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, pos);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.SUCCESS;
    }

    private static void dropInventoryItems(World worldIn, BlockPos pos, IItemHandler inventory) {
        for (int i = 0; i < inventory.getSlots(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (itemstack.getCount() > 0) {
                InventoryHelper.spawnItemStack(worldIn, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), itemstack);
            }
        }
    }
}

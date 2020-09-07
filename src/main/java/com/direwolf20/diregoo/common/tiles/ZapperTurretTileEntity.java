package com.direwolf20.diregoo.common.tiles;

import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.blocks.ZapperTurretBlock;
import com.direwolf20.diregoo.common.container.ZapperTurretContainer;
import com.direwolf20.diregoo.common.entities.GooSpreadEntity;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ZapperTurretTileEntity extends FETileBase implements ITickableTileEntity, INamedContainerProvider {

    private int shootCooldown;
    private int remainingShots;
    private boolean isShooting;

    private BlockPos currentPos = BlockPos.ZERO;

    public ZapperTurretTileEntity() {
        super(ModBlocks.ZAPPERTURRET_TILE.get());
    }

    public boolean isShooting() {
        return isShooting;
    }

    public BlockPos getCurrentPos() {
        return currentPos;
    }

    public int getShootCooldown() {
        return shootCooldown;
    }

    public void setShooting(boolean shooting) {
        isShooting = shooting;
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        assert world != null;
        return new ZapperTurretContainer(this, this.FETileData, i, playerInventory);
    }

    public void clearNextSet(BlockPos startPos) {
        Set<BlockPos> clearBlocksQueue;
        int radius = 2;
        Direction forward = this.getBlockState().get(ZapperTurretBlock.FACING);
        boolean vertical = forward.getAxis().isVertical();
        Direction up = vertical ? Direction.NORTH : Direction.UP;
        Direction down = up.getOpposite();
        Direction side = forward.getOpposite();
        Direction right = vertical ? up.rotateY() : side.rotateYCCW();
        Direction left = right.getOpposite();
        clearBlocksQueue = BlockPos.getAllInBox(startPos.offset(up, radius).offset(left, radius), startPos.offset(down, radius).offset(right, radius))
                .filter(blockPos -> world.getBlockState(blockPos).getBlock() instanceof GooBase)
                .map(BlockPos::toImmutable)
                .collect(Collectors.toCollection(HashSet::new));

        if (clearBlocksQueue.size() != 0) {
            BlockSave blockSave = BlockSave.get(world);
            for (BlockPos pos : clearBlocksQueue) {
                GooBase.resetBlock((ServerWorld) world, pos, true, 40, true, blockSave);
            }
        }
        List<GooSpreadEntity> gooSpreadEntities = world.getEntitiesWithinAABB(GooSpreadEntity.class, new AxisAlignedBB(startPos.offset(up, radius).offset(left, radius), startPos.offset(down, radius).offset(right, radius)));
        for (GooSpreadEntity gooSpreadEntity : gooSpreadEntities)
            gooSpreadEntity.remove();
        remainingShots--;
        shootCooldown = getMaxShootCooldown();
    }

   /* public int getFiringCooldown() {
        return firingCooldown;
    }

    public BlockPos getCurrentTarget() {
        return currentTarget;
    }*/

    public void decrementCooldowns() {
        //if (searchCooldown > 0) searchCooldown--;
        if (shootCooldown > 0) shootCooldown--;
    }

    /*public void decrementFiring() {
        if (firingCooldown > 0) firingCooldown--;
        if (firingCooldown == 0) {
            currentTarget = BlockPos.ZERO;
            markDirtyClient();
        }
    }*/

    public Direction getFacing() {
        return this.getBlockState().get(ZapperTurretBlock.FACING);
    }

    public void beginShooting() {
        if (isShooting()) return;
        isShooting = true;
        remainingShots = 15;
        currentPos = this.pos.offset(getFacing());
        clearNextSet(currentPos);
        markDirtyClient();
    }

    public int getMaxShootCooldown() {
        return 10;
    }

    public void shoot() {
        currentPos = currentPos.offset(getFacing());
        clearNextSet(currentPos);
        if (remainingShots == 0) {
            isShooting = false;
            currentPos = BlockPos.ZERO;
        }
        markDirtyClient();
    }

    /*public void shoot() {
        if (energyStorage.getEnergyStored() < Config.TURRET_RFCOST.get())
            return;
        BlockPos shootPos = clearBlocksQueue.remove();
        int shootDuration = 5;
        if (world.getBlockState(shootPos).getBlock() instanceof GooBase) {
            BlockSave blockSave = BlockSave.get(world);
            GooBase.resetBlock((ServerWorld) world, shootPos, true, shootDuration, true, blockSave);
            firingCooldown = shootDuration;
            currentTarget = shootPos;
            energyStorage.consumeEnergy(Config.TURRET_RFCOST.get(), false);
            markDirtyClient();
        }
    }*/

    @Override
    public void tick() {
        //Client only
        if (world.isRemote) {
            //System.out.println("I'm here!");
        }

        //Server Only
        if (!world.isRemote) {
            energyStorage.receiveEnergy(625, false); //Testing
            if (!isShooting()) return;
            if (shootCooldown > 0) {
                shootCooldown--;
                markDirtyClient();
                return;
            } else {
                shoot();
            }
        }
    }


    //Misc Methods for TE's
    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        shootCooldown = tag.getInt("shootCooldown");
        remainingShots = tag.getInt("remainingShots");
        isShooting = tag.getBoolean("isShooting");
        currentPos = NBTUtil.readBlockPos(tag.getCompound("currentPos"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putInt("shootCooldown", shootCooldown);
        tag.putInt("remainingShots", remainingShots);
        tag.putBoolean("isShooting", isShooting);
        tag.put("currentPos", NBTUtil.writeBlockPos(currentPos));
        return super.write(tag);
    }

    @Nonnull
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.up(10).north(10).east(10), pos.down(10).south(10).west(10));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Turret Tile");
    }
}

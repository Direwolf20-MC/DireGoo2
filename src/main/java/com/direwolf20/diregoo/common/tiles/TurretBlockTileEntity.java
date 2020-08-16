package com.direwolf20.diregoo.common.tiles;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

public class TurretBlockTileEntity extends TileEntity implements ITickableTileEntity {

    Queue<BlockPos> clearBlocksQueue = new LinkedList<>();
    private int searchCooldown;
    private int shootCooldown;
    private int firingCooldown;

    private BlockPos currentTarget = BlockPos.ZERO;

    public TurretBlockTileEntity() {
        super(ModBlocks.TURRETBLOCK_TILE.get());
    }


    public void generateTurretQueue() {
        int range = Config.TURRET_RANGE.get();
        clearBlocksQueue = BlockPos.getAllInBox(this.pos.add(range, -range, range), this.pos.add(-range, range, -range))
                .filter(blockPos -> world.getBlockState(blockPos).getBlock() instanceof GooBase)
                .map(BlockPos::toImmutable)
                .collect(Collectors.toCollection(LinkedList::new));

    }

    public int getFiringCooldown() {
        return firingCooldown;
    }

    public BlockPos getCurrentTarget() {
        return currentTarget;
    }

    public void decrementCooldowns() {
        if (searchCooldown > 0) searchCooldown--;
        if (shootCooldown > 0) shootCooldown--;
    }

    public void decrementFiring() {
        if (firingCooldown > 0) firingCooldown--;
        if (firingCooldown == 0) {
            currentTarget = BlockPos.ZERO;
            markDirtyClient();
        }
    }

    public void shoot() {
        BlockPos shootPos = clearBlocksQueue.remove();
        int shootDuration = 80;
        if (world.getBlockState(shootPos).getBlock() instanceof GooBase) {
            GooBase.resetBlock((ServerWorld) world, shootPos, true, shootDuration);
            firingCooldown = shootDuration;
            currentTarget = shootPos;
            markDirtyClient();
        }
    }

    @Override
    public void tick() {
        //Client only
        if (world.isRemote) {
            //System.out.println("I'm here!");
        }

        //Server Only
        if (!world.isRemote) {
            if (firingCooldown > 0)
                decrementFiring();
            else {
                decrementCooldowns();
                if (searchCooldown == 0 && clearBlocksQueue.isEmpty()) {
                    generateTurretQueue(); //Scan around the turret for blocks to shoot - add to a queue
                    searchCooldown = 200; //Ticks before we can search for goo again
                }
                if (shootCooldown == 0 && !clearBlocksQueue.isEmpty()) {
                    shoot(); //Shoot at the next block in the queue
                    shootCooldown = 20; //Ticks before the turret can shoot again after finishing firing
                }
            }
        }
    }


    //Misc Methods for TE's
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        // Vanilla uses the type parameter to indicate which type of tile entity (command block, skull, or beacon?) is receiving the packet, but it seems like Forge has overridden this behavior
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        func_230337_a_(this.getBlockState(), pkt.getNbtCompound());
    }

    public void markDirtyClient() {
        markDirty();
        if (getWorld() != null) {
            BlockState state = getWorld().getBlockState(getPos());
            getWorld().notifyBlockUpdate(getPos(), state, state, 3);
        }
    }

    @Override
    public void func_230337_a_(BlockState state, CompoundNBT tag) {
        super.func_230337_a_(state, tag);
        currentTarget = NBTUtil.readBlockPos(tag.getCompound("currentTarget"));
        searchCooldown = tag.getInt("searchCooldown");
        shootCooldown = tag.getInt("shootCooldown");
        firingCooldown = tag.getInt("firingCooldown");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("currentTarget", NBTUtil.writeBlockPos(currentTarget));
        tag.putInt("searchCooldown", searchCooldown);
        tag.putInt("shootCooldown", shootCooldown);
        tag.putInt("firingCooldown", firingCooldown);
        return super.write(tag);
    }

    @Nonnull
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.up(10).north(10).east(10), pos.down(10).south(10).west(10));
    }
}

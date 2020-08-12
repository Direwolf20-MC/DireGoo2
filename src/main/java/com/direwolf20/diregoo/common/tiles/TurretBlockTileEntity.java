package com.direwolf20.diregoo.common.tiles;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

public class TurretBlockTileEntity extends TileEntity implements ITickableTileEntity {

    Queue<BlockPos> clearBlocksQueue = new LinkedList<>();
    int searchCooldown;
    int shootCooldown;

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

    public void decrementCooldowns() {
        if (searchCooldown > 0) searchCooldown--;
        if (shootCooldown > 0) shootCooldown--;
    }

    public void shoot() {
        BlockPos shootPos = clearBlocksQueue.remove();
        if (world.getBlockState(shootPos).getBlock() instanceof GooBase) {
            GooBase.resetBlock((ServerWorld) world, shootPos, true);
            System.out.println("Clearing goo at: " + shootPos);
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
            decrementCooldowns();
            if (searchCooldown == 0 && clearBlocksQueue.isEmpty()) {
                System.out.println("Queue Empty - Making new queue");
                generateTurretQueue();
                System.out.println("Found " + clearBlocksQueue.size() + " goo blocks to clear.");
                searchCooldown = 200; //Ticks before we can search for goo again
            }
            if (shootCooldown == 0 && !clearBlocksQueue.isEmpty()) {
                shoot();
                shootCooldown = 1; //Ticks before the turret can shoot again
            }

            //System.out.println("I'm here!");
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

    @Override
    public void func_230337_a_(BlockState state, CompoundNBT tag) {
        super.func_230337_a_(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        return super.write(tag);
    }
}

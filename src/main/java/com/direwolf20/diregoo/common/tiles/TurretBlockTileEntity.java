package com.direwolf20.diregoo.common.tiles;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.container.TurretContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

public class TurretBlockTileEntity extends FETileBase implements ITickableTileEntity, INamedContainerProvider {

    Queue<BlockPos> clearBlocksQueue = new LinkedList<>();
    private int searchCooldown;
    private int shootCooldown;
    private int firingCooldown;

    private BlockPos currentTarget = BlockPos.ZERO;

    public TurretBlockTileEntity() {
        super(ModBlocks.TURRETBLOCK_TILE.get());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        assert world != null;
        return new TurretContainer(this, this.FETileData, i, playerInventory);
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
        if (energyStorage.getEnergyStored() < Config.TURRET_RFCOST.get())
            return;
        BlockPos shootPos = clearBlocksQueue.remove();
        int shootDuration = 20;
        if (world.getBlockState(shootPos).getBlock() instanceof GooBase) {
            GooBase.resetBlock((ServerWorld) world, shootPos, true, shootDuration);
            firingCooldown = shootDuration;
            currentTarget = shootPos;
            energyStorage.consumeEnergy(Config.TURRET_RFCOST.get(), false);
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
            //energyStorage.receiveEnergy(625, false); //Testing
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
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
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

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Turret Tile");
    }
}

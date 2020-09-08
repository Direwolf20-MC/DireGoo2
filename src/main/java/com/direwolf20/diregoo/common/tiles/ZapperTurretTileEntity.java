package com.direwolf20.diregoo.common.tiles;

import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.blocks.ZapperTurretBlock;
import com.direwolf20.diregoo.common.container.ZapperTurretContainer;
import com.direwolf20.diregoo.common.entities.GooSpreadEntity;
import com.direwolf20.diregoo.common.items.CoreFreeze;
import com.direwolf20.diregoo.common.items.CoreMelt;
import com.direwolf20.diregoo.common.items.zapperupgrades.*;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

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
    private Set<BlockPos> clearBlocksQueue = new HashSet<>();
    private BlockPos currentPos = BlockPos.ZERO;
    private ItemStackHandler inventoryStacks = new ItemStackHandler(ZapperTurretContainer.SLOTS);
    private LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(ZapperTurretContainer.SLOTS));

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

    public Set<BlockPos> getClearBlocksQueue() {
        return clearBlocksQueue;
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        assert world != null;
        return new ZapperTurretContainer(this, this.FETileData, i, playerInventory, inventoryStacks);
    }

    public boolean validateSlots() {
        ItemStack core = inventoryStacks.getStackInSlot(0);
        if (!(core.getItem() instanceof CoreMelt) && !(core.getItem() instanceof CoreFreeze))
            return false;

        if (!(inventoryStacks.getStackInSlot(1).getItem() instanceof BaseFocusCrystal))
            return false;

        if (!(inventoryStacks.getStackInSlot(2).getItem() instanceof BasePowerAmp))
            return false;

        return true;
    }

    public int getRadius() {
        if (inventoryStacks.getStackInSlot(1).getItem() instanceof FocusT1) return 1;
        if (inventoryStacks.getStackInSlot(1).getItem() instanceof FocusT2) return 2;
        if (inventoryStacks.getStackInSlot(1).getItem() instanceof FocusT3) return 3;
        if (inventoryStacks.getStackInSlot(1).getItem() instanceof FocusT4) return 4;
        return 0;
    }

    public int getRange() {
        if (inventoryStacks.getStackInSlot(2).getItem() instanceof PowerAmpT1) return 8;
        if (inventoryStacks.getStackInSlot(2).getItem() instanceof PowerAmpT2) return 16;
        if (inventoryStacks.getStackInSlot(2).getItem() instanceof PowerAmpT3) return 24;
        if (inventoryStacks.getStackInSlot(2).getItem() instanceof PowerAmpT4) return 32;
        return 0;
    }

    public void processGoo(ServerWorld world, BlockPos pos) {
        BlockSave blockSave = BlockSave.get(world);
        if (isMelting())
            GooBase.resetBlock(world, pos, true, 10, true, blockSave);
        else
            GooBase.freezeGoo(world, pos);
    }

    public void processNextSet(BlockPos startPos) {
        int radius = getRadius();
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
            for (BlockPos pos : clearBlocksQueue) {
                processGoo((ServerWorld) world, pos);
            }
        }
        List<GooSpreadEntity> gooSpreadEntities = world.getEntitiesWithinAABB(GooSpreadEntity.class, new AxisAlignedBB(startPos.offset(up, radius).offset(left, radius), startPos.offset(down, radius).offset(right, radius)));
        for (GooSpreadEntity gooSpreadEntity : gooSpreadEntities)
            gooSpreadEntity.remove();
        remainingShots--;
        shootCooldown = getMaxShootCooldown();
        markDirtyClient();
    }

    public Direction getFacing() {
        return this.getBlockState().get(ZapperTurretBlock.FACING);
    }

    public void beginShooting() {
        if (isShooting() || !validateSlots()) return;
        isShooting = true;
        remainingShots = getRange();
        currentPos = this.pos.offset(getFacing());
        processNextSet(currentPos);
        markDirtyClient();
    }

    public int getMaxShootCooldown() {
        if (isMelting())
            return 4;
        else
            return 4;
    }

    public boolean isFreezing() {
        return inventoryStacks.getStackInSlot(0).getItem() instanceof CoreFreeze;
    }

    public boolean isMelting() {
        return inventoryStacks.getStackInSlot(0).getItem() instanceof CoreMelt;
    }

    public void shoot() {
        if (!validateSlots()) {
            isShooting = false;
            remainingShots = 0;
            return;
        }
        if (remainingShots <= 0) {
            isShooting = false;
            if (isFreezing()) {
                currentPos = currentPos.offset(getFacing());
                processNextSet(currentPos);
            }
            currentPos = BlockPos.ZERO;
            markDirtyClient();
        }
        currentPos = currentPos.offset(getFacing());
        processNextSet(currentPos);
        markDirtyClient();
    }

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
        inventoryStacks.deserializeNBT(tag.getCompound("inv"));
        //inventory.ifPresent(h -> h.deserializeNBT(tag.getCompound("inv")));
        shootCooldown = tag.getInt("shootCooldown");
        remainingShots = tag.getInt("remainingShots");
        isShooting = tag.getBoolean("isShooting");
        currentPos = NBTUtil.readBlockPos(tag.getCompound("currentPos"));
        clearBlocksQueue.clear();
        ListNBT clearBlocksList = tag.getList("clearBlocksQueue", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < clearBlocksList.size(); i++) {
            BlockPos blockPos = NBTUtil.readBlockPos(clearBlocksList.getCompound(i).getCompound("pos"));
            clearBlocksQueue.add(blockPos);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("inv", inventoryStacks.serializeNBT());
        //inventory.ifPresent(h -> tag.put("inv", h.serializeNBT()));
        tag.putInt("shootCooldown", shootCooldown);
        tag.putInt("remainingShots", remainingShots);
        tag.putBoolean("isShooting", isShooting);
        tag.put("currentPos", NBTUtil.writeBlockPos(currentPos));
        ListNBT list = new ListNBT();
        for (BlockPos pos : clearBlocksQueue) {
            CompoundNBT comp = new CompoundNBT();
            comp.put("pos", NBTUtil.writeBlockPos(pos));
            list.add(comp);
        }
        tag.put("clearBlocksQueue", list);
        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, final @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventory.cast();

        return super.getCapability(cap, side);
    }

    @Nonnull
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.up(32).north(32).east(32), pos.down(32).south(32).west(32));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Turret Tile");
    }
}

package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.container.ZapperItemContainer;
import com.direwolf20.diregoo.common.container.ZapperSlotHandler;
import com.direwolf20.diregoo.common.items.zapperupgrades.*;
import com.direwolf20.diregoo.common.util.VectorHelper;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GooZapper extends FELaserBase {

    public GooZapper() {
        super(new Properties().maxStackSize(1).group(DireGoo.itemGroup), Config.ITEM_ZAPPER_RFMAX.get());
    }

    public boolean validateSlots(ItemStack stack) {
        ZapperSlotHandler inventoryStacks = getInventory(stack);
        ItemStack core = inventoryStacks.getStackInSlot(0);
        if (!(core.getItem() instanceof CoreMelt) && !(core.getItem() instanceof CoreFreeze))
            return false;

        if (!(inventoryStacks.getStackInSlot(1).getItem() instanceof BaseFocusCrystal))
            return false;

        if (!(inventoryStacks.getStackInSlot(2).getItem() instanceof BasePowerAmp))
            return false;

        return true;
    }

    public int getRadius(ItemStack stack) {
        ZapperSlotHandler inventoryStacks = getInventory(stack);
        if (inventoryStacks.getStackInSlot(1).getItem() instanceof FocusT1) return 0;
        if (inventoryStacks.getStackInSlot(1).getItem() instanceof FocusT2) return 1;
        if (inventoryStacks.getStackInSlot(1).getItem() instanceof FocusT3) return 2;
        if (inventoryStacks.getStackInSlot(1).getItem() instanceof FocusT4) return 3;
        return 0;
    }

    public static int getRange(ItemStack stack) {
        ZapperSlotHandler inventoryStacks = getInventory(stack);
        if (inventoryStacks.getStackInSlot(2).getItem() instanceof PowerAmpT1) return 8;
        if (inventoryStacks.getStackInSlot(2).getItem() instanceof PowerAmpT2) return 12;
        if (inventoryStacks.getStackInSlot(2).getItem() instanceof PowerAmpT3) return 16;
        if (inventoryStacks.getStackInSlot(2).getItem() instanceof PowerAmpT4) return 20;
        return 0;
    }

    public Set<BlockPos> findGoo(ItemStack stack, BlockRayTraceResult startBlock, World world) {
        BlockPos startPos = startBlock.getPos();
        Set<BlockPos> clearBlocksQueue = new HashSet<>();
        int radius = getRadius(stack);
        Direction side = startBlock.getFace();
        Direction forward = side.getOpposite();
        boolean vertical = forward.getAxis().isVertical();
        Direction up = vertical ? Direction.NORTH : Direction.UP;
        Direction down = up.getOpposite();
        Direction right = vertical ? up.rotateY() : side.rotateYCCW();
        Direction left = right.getOpposite();
        clearBlocksQueue = BlockPos.getAllInBox(startPos.offset(up, radius).offset(left, radius), startPos.offset(down, radius).offset(right, radius))
                .filter(blockPos -> world.getBlockState(blockPos).getBlock() instanceof GooBase)
                .map(BlockPos::toImmutable)
                .collect(Collectors.toCollection(HashSet::new));
        return clearBlocksQueue;
    }

    public int getRFCost() {
        return Config.ITEM_ZAPPER_RFCOST.get();
    }

    public static boolean isFreezing(ItemStack stack) {
        return getInventory(stack).getStackInSlot(0).getItem() instanceof CoreFreeze;
    }

    public static boolean isMelting(ItemStack stack) {
        return getInventory(stack).getStackInSlot(0).getItem() instanceof CoreMelt;
    }

    public void laserAction(ServerWorld world, BlockPos pos, LivingEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (isMelting(itemstack)) {
            BlockSave blockSave = BlockSave.get(world);
            GooBase.resetBlock(world, pos, true, 20, true, blockSave);
        } else if (isFreezing(itemstack)) {
            GooBase.freezeGoo(world, pos);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (world.isRemote) return new ActionResult<>(ActionResultType.PASS, itemstack);
        if (player.isSneaking()) {
            ZapperSlotHandler handler = getInventory(itemstack);
            NetworkHooks.openGui((ServerPlayerEntity) player, new SimpleNamedContainerProvider(
                    (windowId, playerInventory, playerEntity) -> new ZapperItemContainer(windowId, playerInventory, handler, itemstack), new StringTextComponent("")));
            return new ActionResult<>(ActionResultType.PASS, itemstack);
        } else {
            if (canUseItem(itemstack, getRFCost())) {
                player.setActiveHand(hand);
                return new ActionResult<>(ActionResultType.PASS, itemstack);
            }
        }
        return new ActionResult<>(ActionResultType.FAIL, itemstack);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        //Client and Server
        World world = player.world;

        // Server Side
        if (!world.isRemote) {
            if (!canUseItem(stack, getRFCost())) {
                player.resetActiveHand();
            }
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> e.extractEnergy(getRFCost(), false));

            BlockRayTraceResult lookingAt = VectorHelper.getLookingAt((PlayerEntity) player, RayTraceContext.FluidMode.NONE, getRange(stack));
            if (lookingAt == null || (world.getBlockState(VectorHelper.getLookingAt((PlayerEntity) player, stack, getRange(stack)).getPos()) == Blocks.AIR.getDefaultState()))
                return;

            Hand hand = player.getActiveHand();
            Set<BlockPos> clearBlocksQueue = findGoo(stack, lookingAt, world);
            for (BlockPos pos : clearBlocksQueue) {
                laserAction((ServerWorld) world, pos, player, hand);
            }
        }
    }

    @Override
    public boolean canUseItem(ItemStack itemStack, int RFCost) {
        if (!validateSlots(itemStack)) return false;
        int energy = itemStack.getOrCreateTag().getInt("energy");
        return RFCost <= energy;
    }

    public static ZapperSlotHandler getInventory(ItemStack stack) {
        CompoundNBT compound = stack.getOrCreateTag();
        ZapperSlotHandler handler = new ZapperSlotHandler(ZapperItemContainer.SLOTS, stack);
        handler.deserializeNBT(compound.getCompound("inv"));
        return !compound.contains("inv") ? setInventory(stack, new ZapperSlotHandler(ZapperItemContainer.SLOTS, stack)) : handler;
    }

    public static ZapperSlotHandler setInventory(ItemStack stack, ZapperSlotHandler handler) {
        stack.getOrCreateTag().put("inv", handler.serializeNBT());
        return handler;
    }

}

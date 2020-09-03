package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.util.VectorHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.energy.CapabilityEnergy;

public class FELaserBase extends FEItemBase {
    public FELaserBase(Item.Properties properties) {
        super(properties, 1000000);
    }

    public FELaserBase(Item.Properties properties, int MaxRF) {
        super(properties, MaxRF);
    }

    public int getRange() {
        return 15;
    }

    public int getRFCost() {
        return 1000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);

        if (world.isRemote) return new ActionResult<>(ActionResultType.PASS, itemstack);
        //Server Side Only
        if (player.isSneaking()) {
            // Debug code for free energy
            itemstack.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> e.receiveEnergy(10000, false));
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

            BlockRayTraceResult lookingAt = VectorHelper.getLookingAt((PlayerEntity) player, RayTraceContext.FluidMode.NONE, getRange());
            if (lookingAt == null || (world.getBlockState(VectorHelper.getLookingAt((PlayerEntity) player, stack, getRange()).getPos()) == Blocks.AIR.getDefaultState()))
                return;
            BlockState blockState = world.getBlockState(lookingAt.getPos());
            if (!(blockState.getBlock() instanceof GooBase))
                return;

            laserAction((ServerWorld) world, lookingAt.getPos());
        }
    }

    public void laserAction(ServerWorld world, BlockPos pos) {
        return;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity)
            entityLiving.resetActiveHand();
    }

    //Misc Stuff
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

}

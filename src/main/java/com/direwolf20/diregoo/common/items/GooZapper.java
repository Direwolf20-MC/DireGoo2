package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.util.VectorHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.energy.CapabilityEnergy;

public class GooZapper extends FEItemBase {
    public GooZapper() {
        super(new Properties().maxStackSize(1).group(DireGoo.itemGroup), Config.ITEM_ZAPPER_RFMAX.get());
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
            if (canUseItem(itemstack, Config.ITEM_ZAPPER_RFCOST.get())) {
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
            if (!canUseItem(stack, Config.ITEM_ZAPPER_RFCOST.get())) {
                player.resetActiveHand();
            }
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> e.extractEnergy(Config.ITEM_ZAPPER_RFCOST.get(), false));
            int range = Config.ITEM_ZAPPER_RANGE.get();
            BlockRayTraceResult lookingAt = VectorHelper.getLookingAt((PlayerEntity) player, RayTraceContext.FluidMode.NONE, range);
            if (lookingAt == null || (world.getBlockState(VectorHelper.getLookingAt((PlayerEntity) player, stack, range).getPos()) == Blocks.AIR.getDefaultState()))
                return;
            BlockState blockState = world.getBlockState(lookingAt.getPos());
            if (!(blockState.getBlock() instanceof GooBase))
                return;

            GooBase.resetBlock((ServerWorld) world, lookingAt.getPos(), true, 20);

        }
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

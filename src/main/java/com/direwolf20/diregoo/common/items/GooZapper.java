package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.container.ZapperItemContainer;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

public class GooZapper extends FELaserBase {

    //private LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(ZapperItemContainer.SLOTS));

    public GooZapper() {
        super(new Properties().maxStackSize(1).group(DireGoo.itemGroup), Config.ITEM_ZAPPER_RFMAX.get());
    }

    @Override
    public int getRange() {
        return Config.ITEM_ZAPPER_RANGE.get();
    }

    @Override
    public int getRFCost() {
        return Config.ITEM_ZAPPER_RFCOST.get();
    }

    @Override
    public void laserAction(ServerWorld world, BlockPos pos, LivingEntity player) {
        BlockSave blockSave = BlockSave.get(world);
        GooBase.resetBlock(world, pos, true, 20, true, blockSave);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (!world.isRemote && player.isSneaking()) {
            ZapperItemContainer.ZapperSlotHandler handler = getInventory(itemstack);
            NetworkHooks.openGui((ServerPlayerEntity) player, new SimpleNamedContainerProvider(
                    (windowId, playerInventory, playerEntity) -> new ZapperItemContainer(windowId, playerInventory, handler, itemstack), new StringTextComponent("")));
            return new ActionResult<>(ActionResultType.PASS, itemstack);
        } else {
            return super.onItemRightClick(world, player, hand);
        }
    }

    public static ZapperItemContainer.ZapperSlotHandler getInventory(ItemStack stack) {
        CompoundNBT compound = stack.getOrCreateTag();
        ZapperItemContainer.ZapperSlotHandler handler = new ZapperItemContainer.ZapperSlotHandler(ZapperItemContainer.SLOTS, stack);
        handler.deserializeNBT(compound.getCompound("inv"));
        return !compound.contains("inv") ? setInventory(stack, new ZapperItemContainer.ZapperSlotHandler(ZapperItemContainer.SLOTS, stack)) : handler;
    }

    public static ZapperItemContainer.ZapperSlotHandler setInventory(ItemStack stack, ZapperItemContainer.ZapperSlotHandler handler) {
        stack.getOrCreateTag().put("inv", handler.serializeNBT());
        return handler;
    }

}

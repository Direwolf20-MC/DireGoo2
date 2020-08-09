package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.entities.LaserGunParticleEntity;
import com.direwolf20.diregoo.common.util.VectorHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class GooRemover extends Item {
    public GooRemover() {
        super(new Item.Properties().maxStackSize(1).group(DireGoo.itemGroup));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);


        if (world.isRemote) return new ActionResult<>(ActionResultType.PASS, itemstack);
        //Server Side Only
        spawnLaserGunParticle(player, world, itemstack);

        return new ActionResult<>(ActionResultType.PASS, itemstack);
    }

    private void spawnLaserGunParticle(PlayerEntity player, World world, ItemStack itemstack) {
        Vector3d playerPos = player.getPositionVec().add(0, player.getEyeHeight(), 0);
        Vector3d look = player.getLookVec(); // or getLook(partialTicks)

        BlockRayTraceResult lookAt = VectorHelper.getLookingAt(player, RayTraceContext.FluidMode.NONE, 15);
        Vector3d lookingAt = lookAt.getHitVec();
        //The next 3 variables are directions on the screen relative to the players look direction. So right = to the right of the player, regardless of facing direction.
        Vector3d right = new Vector3d(-look.z, 0, look.x).normalize();
        Vector3d forward = look;
        Vector3d backward = look.mul(-1, 1, -1);
        Vector3d down = right.crossProduct(forward);

        //Take the player's eye position, and shift it to where the end of the laser is (Roughly)
        right = right.scale(MathHelper.lerp((look.z + 1) / 2, 0.55f, 0.80f));
        forward = forward.scale(1.25f);
        down = down.scale(MathHelper.lerp((look.x + 1) / 2, -0.35, -0.45));
        backward = backward.scale(0.05);
        Vector3d laserPos = playerPos.add(right);
        laserPos = laserPos.add(forward);
        laserPos = laserPos.add(down);
        lookingAt = lookingAt.add(backward);

        Vector3d pathVec = lookingAt.add(-0.125, -0.125, -0.125).subtract(laserPos).normalize(); //Half of the particle size, so it adjusts to the actual center of the block

        LaserGunParticleEntity laserGunParticleEntity = new LaserGunParticleEntity(world, player, laserPos.getX(), laserPos.getY(), laserPos.getZ());
        laserGunParticleEntity.shoot(pathVec.getX(), pathVec.getY(), pathVec.getZ(), 1f, 0f);
        world.addEntity(laserGunParticleEntity);

    }
}

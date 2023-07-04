package lanse505.culinarium.common.item;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.register.CulinariumItemRegistry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class HandSieve extends Item {

  private int sieveTime = 0;

  public HandSieve(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public UseAnim getUseAnimation(ItemStack pStack) {
    return UseAnim.BLOCK;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
    pPlayer.startUsingItem(pUsedHand);
    return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
  }

  @Override
  public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
    ItemStack mainhand = pLivingEntity.getItemInHand(InteractionHand.MAIN_HAND);
    if (mainhand.is(CulinariumItemRegistry.HAND_SIEVE.get())) {
      if (mainhand.getUseDuration() - sieveTime <= 0) {
        ItemStack offhand = pLivingEntity.getItemInHand(InteractionHand.OFF_HAND);
        if (offhand.is(CulinariumItemRegistry.MIXED_WHEAT_BERRIES.get())) {
          if (!pLevel.isClientSide) {
            offhand.shrink(1);
            RandomSource random = pLevel.getRandom();
            int huskCount = random.nextIntBetweenInclusive(1, 3);
            ItemStack husks = new ItemStack(CulinariumItemRegistry.WHEAT_HUSKS.get(), huskCount);
            dropItem(husks, pLivingEntity, pLevel);
            int berriesCount = random.nextIntBetweenInclusive(2, 4);
            ItemStack berries = new ItemStack(CulinariumItemRegistry.WHEAT_BERRIES.get(), berriesCount);
            dropItem(berries, pLivingEntity, pLevel);
            return;
          }
        }
        sieveTime = 0;
      } else {
        sieveTime++;
      }
    }
  }

  public void dropItem(ItemStack pStack, LivingEntity pLivingEntity, Level pLevel) {
    if (!pLevel.isClientSide) {
      ItemEntity entity = new ItemEntity(pLevel, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), pStack);
      entity.setDeltaMovement(
              pLevel.random.triangle(0.0D, 0.11485000171139836D),
              pLevel.random.triangle(0.2D, 0.11485000171139836D),
              pLevel.random.triangle(0.0D, 0.11485000171139836D)
      );
      pLevel.addFreshEntity(entity);
    }
  }

  @Override
  public int getUseDuration(ItemStack pStack) {
    return 30;
  }
}

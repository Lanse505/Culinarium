package lanse505.culinarium.common.events;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.register.CulinariumItemRegistry;
import lanse505.culinarium.common.util.CulinariumTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Culinarium.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ThrashingHandler {

  private static final Object2IntOpenHashMap<ItemStack> thrashingMap = new Object2IntOpenHashMap<>();

  public ThrashingHandler() {}

  // TODO: Add Particles
  @SubscribeEvent
  public static void onThrashing(PlayerInteractEvent.RightClickBlock event) {
    boolean isMainHand = event.getHand() == InteractionHand.MAIN_HAND;
    boolean isValidGrain = event.getItemStack().is(Items.WHEAT) || event.getItemStack().is(CulinariumItemRegistry.RYE.get());
    boolean isCrouching = event.getEntity().isCrouching();
    if (isValidGrain && isMainHand && isCrouching) {
      ItemStack stack = event.getItemStack();
      BlockPos pos = event.getHitVec().getBlockPos();
      BlockState state = event.getLevel().getBlockState(pos);
      boolean isHardSurface = state.is(CulinariumTags.CulinariumBlockTags.HARD_SURFACE) || state.getBlock().getExplosionResistance(state, event.getLevel(), pos, null) > 6.0f;
      if (isHardSurface) {
        if (thrashingMap.containsKey(stack)) {
          int thrashCount = thrashingMap.getInt(stack);
          if (thrashCount >= 4) {
            thrashingMap.removeInt(stack);
            ItemEntity straw = new ItemEntity(
                    event.getLevel(),
                    event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                    new ItemStack(CulinariumItemRegistry.STRAW.get())
            );
            int mixedCount = event.getLevel().getRandom().nextIntBetweenInclusive(1, 3);
            ItemEntity mixed = new ItemEntity(
                    event.getLevel(),
                    event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                    new ItemStack(CulinariumItemRegistry.MIXED_WHEAT_BERRIES.get(), mixedCount)
            );
            event.getLevel().addFreshEntity(straw);
            event.getLevel().addFreshEntity(mixed);
            return;
          }
          thrashingMap.put(stack, thrashingMap.getInt(stack) + 1);
        } else {
          thrashingMap.put(stack, 1);
        }
        event.getEntity().swing(event.getHand());
        event.setResult(Event.Result.ALLOW);
      }
    }
  }
}

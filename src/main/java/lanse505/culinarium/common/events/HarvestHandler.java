package lanse505.culinarium.common.events;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lanse505.culinarium.Culinarium;
import lanse505.culinarium.server.recipe.HarvestRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Culinarium.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HarvestHandler {

  private static final Cache<BlockState, HarvestRecipe> HARVEST_CACHE = CacheBuilder.newBuilder()
          .expireAfterAccess(90, java.util.concurrent.TimeUnit.SECONDS)
          .maximumSize(100)
          .build();

  @SubscribeEvent
  public static void onHarvest(PlayerInteractEvent.RightClickBlock event) {
    Level level = event.getLevel();
    BlockPos pos = event.getPos();
    BlockState state = level.getBlockState(pos);
    HarvestRecipe hasRecipe = HARVEST_CACHE.asMap().computeIfAbsent(state,
            blockState -> level.getRecipeManager().getRecipes().stream()
                    .filter(HarvestRecipe.class::isInstance)
                    .map(HarvestRecipe.class::cast)
                    .filter(recipe -> recipe.isValid(state))
                    .peek(recipe -> HARVEST_CACHE.put(state, recipe))
                    .findFirst().orElse(null));
    boolean handCheck = event.getHand() == InteractionHand.MAIN_HAND && (event.getItemStack().isEmpty() || event.getItemStack().is(Items.BONE_MEAL));
    if (hasRecipe != null && handCheck) {
      hasRecipe.harvest(level, state, pos);
      event.getEntity().swing(event.getHand());
      event.setCanceled(true);
    }
  }
}

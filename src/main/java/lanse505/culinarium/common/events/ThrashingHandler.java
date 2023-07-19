package lanse505.culinarium.common.events;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.register.CulinariumItemRegistry;
import lanse505.culinarium.common.util.CulinariumTags;
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

    // TODO: Refactor to take into account, Player, ItemStack, Level for the key
    private static final Cache<ItemStack, Integer> thrashingCache = CacheBuilder.newBuilder()
            .expireAfterAccess(60, java.util.concurrent.TimeUnit.SECONDS)
            .maximumSize(100)
            .build();

    // TODO: Add Particles
    @SubscribeEvent
    public static void onThrashing(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        BlockState state = event.getLevel().getBlockState(event.getHitVec().getBlockPos());

        if (event.getHand() != InteractionHand.MAIN_HAND || !event.getEntity().isCrouching() ||
                (!stack.is(Items.WHEAT) && !stack.is(CulinariumItemRegistry.RYE.get())) ||
                !(state.is(CulinariumTags.CulinariumBlockTags.HARD_SURFACE) || state.getBlock().getExplosionResistance(state, event.getLevel(), event.getHitVec().getBlockPos(), null) > 6.0f)) {
            return;
        }

        Integer thrashCount = thrashingCache.getIfPresent(stack);
        thrashingCache.put(stack, thrashCount != null ? thrashCount + 1 : 0);
        if (thrashingCache.getIfPresent(stack) >= 4) {
            thrashingCache.invalidate(stack);
            ItemStack strawStack = new ItemStack(CulinariumItemRegistry.STRAW.get());
            ItemStack mixedStack = new ItemStack(CulinariumItemRegistry.MIXED_WHEAT_BERRIES.get(),
                    event.getLevel().getRandom().nextIntBetweenInclusive(1, 3));
            ItemEntity straw = new ItemEntity(event.getLevel(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), strawStack);
            ItemEntity mixed = new ItemEntity(event.getLevel(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), mixedStack);
            event.getLevel().addFreshEntity(straw);
            event.getLevel().addFreshEntity(mixed);
            return;
        }

        event.getEntity().swing(event.getHand());
        event.setResult(Event.Result.ALLOW);
    }
}

package lanse505.culinarium.data.provider.loot;

import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import lanse505.culinarium.common.register.CulinariumItemRegistry;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class CulinariumLootTableProvider extends LootTableProvider {

  public CulinariumLootTableProvider(PackOutput pOutput) {
    super(pOutput, Set.of(), List.of(
            new LootTableProvider.SubProviderEntry(CulinariumBlockLoot::new, LootContextParamSets.BLOCK)
    ));
  }

  @Override
  protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationcontext) {
    // Do not validate against all registered loot tables
  }

  public static class CulinariumBlockLoot extends BlockLootSubProvider {

    private static final Set<Item> EXPLOSION_RESISTANT = Collections.emptySet();

    protected CulinariumBlockLoot() {
      super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
      LootItemCondition.Builder ryeCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(CulinariumBlockRegistry.RYE.get())
              .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7));
      this.dropSelf(CulinariumBlockRegistry.MILLSTONE.getBlock());
      this.add(CulinariumBlockRegistry.RYE.get(),
              this.createCropDrops(
                      CulinariumBlockRegistry.RYE.get(),
                      CulinariumItemRegistry.RYE.get(),
                      CulinariumItemRegistry.RYE_SEEDS.get(),
                      ryeCondition
              )
      );
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> writer) {
      this.generate();
      for (Map.Entry<ResourceLocation, LootTable.Builder> entry : this.map.entrySet()) {
        writer.accept(entry.getKey(), entry.getValue());
      }
    }


    @Override
    protected Iterable<Block> getKnownBlocks() {
      return CulinariumBlockRegistry.BLOCKS.getEntries().stream().flatMap(RegistryObject::stream)::iterator;
    }
  }

}

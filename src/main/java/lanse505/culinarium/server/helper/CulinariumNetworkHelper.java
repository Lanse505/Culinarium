package lanse505.culinarium.server.helper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

public class CulinariumNetworkHelper {
  public static BlockState readBlockState(FriendlyByteBuf buffer) {
    return NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), buffer.readNbt());
  }
  
}

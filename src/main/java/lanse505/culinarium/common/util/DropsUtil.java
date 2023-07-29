package lanse505.culinarium.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DropsUtil {

  public static Vec3 getDropDelta(BlockPos pos, Level level) {
    double d0 = (double) EntityType.ITEM.getWidth();
    double d1 = 1.0D - d0;
    double d2 = d0 / 2.0D;
    double d3 = pos.getX() + level.random.nextDouble() * d1 + d2;
    double d4 = pos.getY() + level.random.nextDouble() * d1;
    double d5 = pos.getZ() + level.random.nextDouble() * d1 + d2;
    return new Vec3(d3, d4, d5);
  }
}

package lanse505.culinarium.common.block.base.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DistanceRayTraceResult extends HitResult {
  private double distance;
  private VoxelShape hitInfo;

  public DistanceRayTraceResult(Vec3 hitVecIn, Direction sideHitIn, BlockPos blockPosIn, VoxelShape box, double distance) {
    super(hitVecIn);
    this.hitInfo = box;
    this.distance = distance;
  }

  public VoxelShape getHitBox() {
    return hitInfo;
  }

  public double getDistance() {
    return distance;
  }

  @Override
  public Type getType() {
    return Type.BLOCK;
  }
}

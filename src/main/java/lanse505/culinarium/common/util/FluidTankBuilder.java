package lanse505.culinarium.common.util;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class FluidTankBuilder {
  protected Predicate<FluidStack> validator;
  @NotNull
  protected FluidStack fluid = FluidStack.EMPTY;
  protected int capacity;

  private Predicate<FluidStack> insertPredicate = fluid -> true;
  private Predicate<FluidStack> extractPredicate = fluid -> true;

  protected FluidTankBuilder(int capacity) {
    this.capacity = capacity;
    this.validator = fluid -> true;
  }

  public static FluidTankBuilder builder(int capacity) {
    return new FluidTankBuilder(capacity);
  }

  public FluidTankBuilder setValidator(Predicate<FluidStack> validator) {
    this.validator = validator;
    return this;
  }

  public FluidTankBuilder setInsertPredicate(Predicate<FluidStack> insertPredicate) {
    this.insertPredicate = insertPredicate;
    return this;
  }

  public FluidTankBuilder setExtractPredicate(Predicate<FluidStack> extractPredicate) {
    this.extractPredicate = extractPredicate;
    return this;
  }

  public FluidTank build() {
    return new FluidTank(this.capacity, this.validator) {
      @Override
      public int fill(FluidStack resource, FluidAction action) {
        if (insertPredicate.test(resource)) {
          return super.fill(resource, action);
        }
        return 0;
      }

      @Override
      public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        if (extractPredicate.test(fluid)) {
          return super.drain(maxDrain, action);
        }
        return FluidStack.EMPTY;
      }
    };
  }
}

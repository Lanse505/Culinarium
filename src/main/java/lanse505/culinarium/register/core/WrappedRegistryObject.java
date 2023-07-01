package lanse505.culinarium.register.core;

import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Supplier;

public class WrappedRegistryObject<T> implements Supplier<T>, INamedEntry {

  protected RegistryObject<T> registryObject;

  protected WrappedRegistryObject(RegistryObject<T> registryObject) {
    this.registryObject = registryObject;
  }

  @Override
  public T get() {
    return registryObject.get();
  }

  @Override
  public String getInternalRegistryName() {
    return registryObject.getId().getPath();
  }

  public ResourceKey<T> key() {
    return Objects.requireNonNull(registryObject.getKey(), "Resource key should not be null");
  }
}

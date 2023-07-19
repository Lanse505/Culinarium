package lanse505.culinarium.common.register.core;

public interface INamedEntry {
    /**
     * Used for retrieving the path/name of a registry object before the registry object has been fully initialized
     */
    String getInternalRegistryName();
}

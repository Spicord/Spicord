package eu.mcdb.spicord.api.services;

public interface ServiceManager {

    /**
     * Registers a new service.
     * <br>
     * Example: registerService(LinkingService.class, myLinkingServiceInstance);
     * 
     * @param serviceClass the type of service provided
     * @param service your service instance
     * @return true if the service was successfully registered
     */
    boolean registerService(Class<? extends Service> serviceClass, Service service);

    /**
     * Checks if a service provider was registered.
     * <br>
     * For example {@code isServiceRegistered(LinkingService.class)} will return true if a provider for {@link LinkingService} is registered.
     * 
     * @param serviceClass the service to be checked
     * @return true if the service is registered
     */
    boolean isServiceRegistered(Class<? extends Service> serviceClass);

    /**
     * Unregisters a previously registered service provider and it becomes available for further registration.
     * <br>
     * For example {@code unregisterService(LinkingService.class)} will return true if the provider for {@link LinkingService} was unregistered. 
     * 
     * @param serviceClass the service to be unregistered
     * @return true if the service was successfully unregistered
     */
    boolean unregisterService(Class<? extends Service> serviceClass);

    /**
     * 
     * @param <T>
     * @param serviceClass
     * @return
     */
    <T extends Service> T getService(Class<? extends Service> serviceClass);

    /**
     * 
     * @param <T>
     * @param id
     * @return
     */
    <T extends Service> T getService(String id);
}

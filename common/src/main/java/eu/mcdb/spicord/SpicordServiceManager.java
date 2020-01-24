package eu.mcdb.spicord;

import java.util.HashMap;
import java.util.Map;
import eu.mcdb.spicord.api.services.Service;
import eu.mcdb.spicord.api.services.ServiceManager;
import lombok.NonNull;

@SuppressWarnings("unchecked")
public class SpicordServiceManager implements ServiceManager {

    private final Map<Class<? extends Service>, Service> services;

    SpicordServiceManager() {
        this.services = new HashMap<>();
    }

    @Override
    public boolean registerService(@NonNull Class<? extends Service> serviceClass, @NonNull Service service) {
        return services.putIfAbsent(serviceClass, service) == null;
    }

    @Override
    public boolean isServiceRegistered(@NonNull Class<? extends Service> serviceClass) {
        return services.containsKey(serviceClass);
    }

    @Override
    public boolean unregisterService(@NonNull Class<? extends Service> serviceClass) {
        return services.remove(serviceClass, getService(serviceClass));
    }

    @Override
    public <T extends Service> T getService(Class<? extends Service> serviceClass) {
        return (T) services.get(serviceClass);
    }

    @Override
    public <T extends Service> T getService(@NonNull String id) {
        return (T) services.values().stream().filter(s -> s.id().equals(id)).findFirst().orElse(null);
    }
}

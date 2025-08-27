package com.experimentcode.loadbalancer;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancer {

    private final Map<String, List<URI>> serviceRegistry = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> state = new ConcurrentHashMap<>();

    public void registerService(String name, URI uri){
        if(uri==null){
            throw new IllegalStateException("URL not specified for" + name);
        }
        serviceRegistry.computeIfAbsent(name, k -> new CopyOnWriteArrayList<>()).add(uri);
        state.putIfAbsent(name, new AtomicInteger(0));
    }

    public void deregisterService(String name, URI uri){
        List<URI> uris = serviceRegistry.get(name);
        if(uris!=null){
            uris.remove(uri);
            if(uris.isEmpty()){
                serviceRegistry.remove(name);
                state.remove(name);
            }
        }
    }

    private boolean isInvalid(String name) {
        return serviceRegistry.get(name)==null || serviceRegistry.get(name).isEmpty();
    }

    private URI getURI(String name){
        List<URI> uris = serviceRegistry.get(name);
       int index = Math.abs(state.get(name).getAndIncrement() % uris.size());
       return uris.get(index);
    }

    public URI forwardRequest(String name) throws IllegalStateException{
        if(isInvalid(name)){
            throw new IllegalStateException("Invalid request");
        }
        return getURI(name);
    }

    public Map<String, List<URI>> getServiceRegistry(){
        return Collections.unmodifiableMap(serviceRegistry);
    }

}

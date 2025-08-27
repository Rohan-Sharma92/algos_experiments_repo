package com.experimentcode.loadbalancer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancer {

    private final Map<String, List<URL>> serviceRegistry = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> state = new ConcurrentHashMap<>();

    public void registerService(String name, URL url){
        if(url==null){
            throw new IllegalStateException("URL not specified for" + name);
        }
        serviceRegistry.putIfAbsent(name, new ArrayList<>());
        List<URL> urls = serviceRegistry.get(name);
        synchronized (urls){
            serviceRegistry.get(name).add(url);
            state.putIfAbsent(name, new AtomicInteger(0));
        }
    }

    public void deRegisterService(String name, URL url){
        if(url==null || isInvalid(name)){
            throw new IllegalStateException("URL not specified for" + name);
        }
        List<URL> urls = serviceRegistry.get(name);
        synchronized (urls){
            urls.remove(url);
            if(urls.isEmpty()){
                serviceRegistry.remove(name);
                state.remove(name);
            }
        }
    }

    private boolean isInvalid(String name) {
        return serviceRegistry.get(name)==null || serviceRegistry.get(name).isEmpty();
    }

    private URL getURL(String name){
        List<URL> urls = serviceRegistry.get(name);
       int index = Math.abs(state.get(name).getAndIncrement() % urls.size());
       return urls.get(index);
    }

    public URL forwardRequest(String name) throws IllegalStateException{
        if(isInvalid(name)){
            throw new IllegalStateException("Invalid request");
        }
        return getURL(name);
    }

}

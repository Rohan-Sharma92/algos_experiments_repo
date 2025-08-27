package com.experimentcode.loadbalancer;

import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class LoadBalancerTest {

    private final LoadBalancer loadBalancer = new LoadBalancer();

    @Test
    public void testForwardRequestWithRegisterOfSameService() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);
        CompletableFuture<Object> serv1 = CompletableFuture.supplyAsync(() -> {
            try {
                latch.await();
                loadBalancer.registerService("serv", URI.create("http://a/b/c/g"));
                return null;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
        CompletableFuture<Object> serv2 = CompletableFuture.supplyAsync(() -> {
            try {
                latch.await();
                loadBalancer.registerService("serv", URI.create("http://a/b/c/h"));
                return null;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
        latch.countDown();
        executorService.shutdown();
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(serv1, serv2);
        completableFuture.join();
        Map<String, List<URI>> serviceRegistry = loadBalancer.getServiceRegistry();
        List<URI> uriList = serviceRegistry.get("serv");
        Assert.assertEquals(uriList.size(), 2);
    }
}

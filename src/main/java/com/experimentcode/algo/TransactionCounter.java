package com.experimentcode.algo;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * You are implementing a transaction counter for Adyen’s payments platform.
 * <p>
 * 1. Multiple threads may increment counts for merchants when a transaction occurs.
 * 2. You must also support querying the total number of transactions per merchant in the last N seconds.
 */
public class TransactionCounter {

    private final Map<String, ConcurrentLinkedDeque<Transaction>> cache = new ConcurrentHashMap<>();

    private final long windowSize;

    TransactionCounter(long windowSize){
        this.windowSize = windowSize;
    }

    // record a transaction at given timestamp (in seconds)
    public void increment(String merchantId, BigDecimal amount, long timestamp){
        ConcurrentLinkedDeque<Transaction> deque = cache.computeIfAbsent(merchantId, String -> new ConcurrentLinkedDeque<>());
        deque.addLast(new Transaction(amount, merchantId, timestamp));
        evictOldTransactions(deque, timestamp);
    }

    private void evictOldTransactions(ConcurrentLinkedDeque<Transaction> deque, long timestamp) {
        while(!deque.isEmpty() && (timestamp - deque.peekFirst().getTimestamp()>windowSize)){
            deque.pollFirst();
        }
    }

    // return number of transactions for merchant in last windowInSeconds seconds
    public int getCount(String merchantId, long currentTimestamp){
        ConcurrentLinkedDeque<Transaction> deque = cache.get(merchantId);
        if(deque==null)
            return 0;
        evictOldTransactions(deque, currentTimestamp);
        if(deque.isEmpty()){
            cache.remove(merchantId);
        }
        return deque.size();
    }
}

class Transaction{
        private final BigDecimal amount;
        private final String key;
        private final long timestamp;

    Transaction(BigDecimal amount, String key, long timestamp) {
        this.amount = amount;
        this.key = key;
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getKey() {
        return key;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int window = sc.nextInt();
        int q = sc.nextInt();
        sc.nextLine();

        TransactionCounter counter = new TransactionCounter(window);

        for (int i = 0; i < q; i++) {
            String[] parts = sc.nextLine().split(" ");
            if (parts[0].equals("INC")) {
                counter.increment(parts[1],
                        new BigDecimal(parts[3]),
                        Long.parseLong(parts[2]));
            } else if (parts[0].equals("GET")) {
                long max = counter.getCount(parts[1], Long.parseLong(parts[2]));
                System.out.println(max);
            }
        }
    }
}

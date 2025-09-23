package com.experimentcode.concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CustomThreadPool {
    private final int poolSize;
    private final WorkerThread[] workers;
    private final BlockingQueue<Runnable> taskQueue;
    private volatile boolean isShutdown = false;

    public CustomThreadPool(int poolSize) {
        this.poolSize = poolSize;
        taskQueue = new LinkedBlockingQueue<>();
        workers = new WorkerThread[poolSize];

        // Start worker threads
        for (int i = 0; i < poolSize; i++) {
            workers[i] = new WorkerThread("ThreadPool-Worker-" + i);
            workers[i].start();
        }
    }

    // Submit a task
    public void execute(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("ThreadPool is shutting down, cannot accept new tasks.");
        }
        taskQueue.offer(task); // Non-blocking insert
    }

    // Graceful shutdown
    public void shutdown() {
        isShutdown = true;
        for (WorkerThread worker : workers) {
            worker.interrupt(); // Wake up workers blocked on queue
        }
    }

    private class WorkerThread extends Thread {
        public WorkerThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            try {
                while (!isShutdown || !taskQueue.isEmpty()) {
                    Runnable task = taskQueue.take(); // Blocks until a task is available
                    try {
                        task.run();
                    } catch (RuntimeException e) {
                        System.err.println("Task execution failed: " + e.getMessage());
                    }
                }
            } catch (InterruptedException e) {
                // Exit gracefully when shutdown
            }
        }
    }

    // Example usage
    public static void main(String[] args) {
        CustomThreadPool pool = new CustomThreadPool(3);

        for (int i = 0; i < 10; i++) {
            int taskId = i;
            pool.execute(() -> {
                System.out.println("Task " + taskId + " executed by " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}
            });
        }

        pool.shutdown();
    }
}

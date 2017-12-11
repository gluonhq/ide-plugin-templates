package com.gluonhq.plugin.dialogs;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class DialogUtils {
    
    public static <T> CompletableFuture<T> supplyAsync(Callable<T> callable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try { 
                future.complete(callable.call()); 
            } catch (Exception t) { 
                future.completeExceptionally(t); 
            }
        });
        return future;
    }
}

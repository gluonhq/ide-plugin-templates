package com.gluonhq.plugin.dialogs;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DialogUtils {
    
    private static final Logger LOGGER = Logger.getLogger(DialogUtils.class.getName());
    
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
    
    public static void openURL(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                new Thread(() -> {
                    try {
                        desktop.browse(new URI(url));
                    } catch (IOException | URISyntaxException ex) {
                        LOGGER.log(Level.SEVERE, "Error launching browser", ex);
                    }
                }).start();
            }
        }
    } 
    
}

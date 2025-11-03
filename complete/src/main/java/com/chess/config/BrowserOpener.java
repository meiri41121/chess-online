package com.chess.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;
import java.io.IOException;

@Component
public class BrowserOpener {
    
    private final Environment environment;

    public BrowserOpener(Environment environment) {
        this.environment = environment;
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        // Check if we're running locally
        if (isLocalEnvironment()) {
            String url = "http://localhost:8080";
            String os = System.getProperty("os.name").toLowerCase();
            
            try {
                if (os.contains("mac")) {
                    Runtime.getRuntime().exec(new String[] { "open", url });
                } else if (os.contains("win")) {
                    Runtime.getRuntime().exec(new String[] { "cmd", "/c", "start", url });
                } else if (os.contains("nix") || os.contains("nux")) {
                    Runtime.getRuntime().exec(new String[] { "xdg-open", url });
                }
                System.out.println("Opening browser at: " + url);
            } catch (IOException e) {
                System.err.println("Failed to open browser: " + e.getMessage());
                System.err.println("Please open " + url + " manually in your browser");
            }
        } else {
            System.out.println("Running in non-local environment (e.g., AWS). Browser opener disabled.");
        }
    }

    private boolean isLocalEnvironment() {
        String[] activeProfiles = environment.getActiveProfiles();
        
        // Check if we're running in a container
        boolean isContainer = System.getenv().containsKey("KUBERNETES_SERVICE_HOST") 
            || System.getenv().containsKey("DOCKER_CONTAINER")
            || Boolean.parseBoolean(System.getenv().getOrDefault("IS_CONTAINER", "false"));

        // Check active profiles
        boolean isProduction = false;
        for (String profile : activeProfiles) {
            if (profile.equalsIgnoreCase("prod") || 
                profile.equalsIgnoreCase("production") || 
                profile.equalsIgnoreCase("aws")) {
                isProduction = true;
                break;
            }
        }

        return !isContainer && !isProduction;
    }
} 
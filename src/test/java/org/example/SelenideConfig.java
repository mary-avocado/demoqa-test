package org.example;

import com.codeborne.selenide.Configuration;
public class SelenideConfig {
    public static void apply() {
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        Configuration.browserSize = System.getProperty("browserSize", "1920x1080");

        String remote = System.getProperty("selenide.remote");
        if (remote != null && !remote.isBlank()) {
            Configuration.remote = remote; // http://localhost:4444/wd/hub
            Configuration.browserCapabilities.setCapability("enableVNC", true);
            Configuration.browserCapabilities.setCapability("enableVideo", false);
        }

        Configuration.timeout = Long.parseLong(System.getProperty("timeoutMs", "6000"));
    }
}

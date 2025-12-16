package com.florianthom.pdfassemblyshowcase.config;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PlaywrightConfig {

    @Bean(destroyMethod = "close")
    public Playwright playwright() {
        return Playwright.create();
    }

    @Bean(destroyMethod = "close")
    public Browser chromiumBrowser(Playwright playwright) {
        return playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(true)
                .setArgs(List.of("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage")));
    }

    @Bean
    public BrowserContext browserContext(Browser chromiumBrowser) {
        return chromiumBrowser.newContext(new Browser.NewContextOptions());
    }
}
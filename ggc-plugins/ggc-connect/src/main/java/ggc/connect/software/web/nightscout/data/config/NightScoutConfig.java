package ggc.connect.software.web.nightscout.data.config;

import ggc.connect.software.web.nightscout.data.defs.BrowserType;

/**
 * Created by andy on 3/30/18.
 */
public class NightScoutConfig {

    private String siteUrl;

    private String apiSecret;

    private BrowserType browserType;

    private String customBrowserPath;

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public BrowserType getBrowserType() {
        return browserType;
    }

    public void setBrowserType(BrowserType browserType) {
        this.browserType = browserType;
    }

    public String getCustomBrowserPath() {
        return customBrowserPath;
    }

    public void setCustomBrowserPath(String customBrowserPath) {
        this.customBrowserPath = customBrowserPath;
    }
}
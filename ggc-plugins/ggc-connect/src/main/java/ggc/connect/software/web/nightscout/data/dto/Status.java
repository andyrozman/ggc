
package ggc.connect.software.web.nightscout.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ggc.connect.software.web.nightscout.data.dto.status.ExtendedSettings;
import ggc.connect.software.web.nightscout.data.dto.status.Settings;

public class Status {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("serverTime")
    @Expose
    private String serverTime;
    @SerializedName("serverTimeEpoch")
    @Expose
    private Integer serverTimeEpoch;
    @SerializedName("apiEnabled")
    @Expose
    private Boolean apiEnabled;
    @SerializedName("careportalEnabled")
    @Expose
    private Boolean careportalEnabled;
    @SerializedName("boluscalcEnabled")
    @Expose
    private Boolean boluscalcEnabled;
    @SerializedName("head")
    @Expose
    private String head;
    @SerializedName("settings")
    @Expose
    private Settings settings;
    @SerializedName("extendedSettings")
    @Expose
    private ExtendedSettings extendedSettings;
    @SerializedName("authorized")
    @Expose
    private Object authorized;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public Integer getServerTimeEpoch() {
        return serverTimeEpoch;
    }

    public void setServerTimeEpoch(Integer serverTimeEpoch) {
        this.serverTimeEpoch = serverTimeEpoch;
    }

    public Boolean getApiEnabled() {
        return apiEnabled;
    }

    public void setApiEnabled(Boolean apiEnabled) {
        this.apiEnabled = apiEnabled;
    }

    public Boolean getCareportalEnabled() {
        return careportalEnabled;
    }

    public void setCareportalEnabled(Boolean careportalEnabled) {
        this.careportalEnabled = careportalEnabled;
    }

    public Boolean getBoluscalcEnabled() {
        return boluscalcEnabled;
    }

    public void setBoluscalcEnabled(Boolean boluscalcEnabled) {
        this.boluscalcEnabled = boluscalcEnabled;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public ExtendedSettings getExtendedSettings() {
        return extendedSettings;
    }

    public void setExtendedSettings(ExtendedSettings extendedSettings) {
        this.extendedSettings = extendedSettings;
    }

    public Object getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Object authorized) {
        this.authorized = authorized;
    }

}

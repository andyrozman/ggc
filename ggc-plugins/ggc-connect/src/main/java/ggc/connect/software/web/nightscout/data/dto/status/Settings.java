
package ggc.connect.software.web.nightscout.data.dto.status;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Settings {

    @SerializedName("units")
    @Expose
    private String units;
    @SerializedName("timeFormat")
    @Expose
    private Integer timeFormat;
    @SerializedName("nightMode")
    @Expose
    private Boolean nightMode;
    @SerializedName("editMode")
    @Expose
    private String editMode;
    @SerializedName("showRawbg")
    @Expose
    private String showRawbg;
    @SerializedName("customTitle")
    @Expose
    private String customTitle;
    @SerializedName("theme")
    @Expose
    private String theme;
    @SerializedName("alarmUrgentHigh")
    @Expose
    private Boolean alarmUrgentHigh;
    @SerializedName("alarmUrgentHighMins")
    @Expose
    private List<Integer> alarmUrgentHighMins = null;
    @SerializedName("alarmHigh")
    @Expose
    private Boolean alarmHigh;
    @SerializedName("alarmHighMins")
    @Expose
    private List<Integer> alarmHighMins = null;
    @SerializedName("alarmLow")
    @Expose
    private Boolean alarmLow;
    @SerializedName("alarmLowMins")
    @Expose
    private List<Integer> alarmLowMins = null;
    @SerializedName("alarmUrgentLow")
    @Expose
    private Boolean alarmUrgentLow;
    @SerializedName("alarmUrgentLowMins")
    @Expose
    private List<Integer> alarmUrgentLowMins = null;
    @SerializedName("alarmUrgentMins")
    @Expose
    private List<Integer> alarmUrgentMins = null;
    @SerializedName("alarmWarnMins")
    @Expose
    private List<Integer> alarmWarnMins = null;
    @SerializedName("alarmTimeagoWarn")
    @Expose
    private Boolean alarmTimeagoWarn;
    @SerializedName("alarmTimeagoWarnMins")
    @Expose
    private Integer alarmTimeagoWarnMins;
    @SerializedName("alarmTimeagoUrgent")
    @Expose
    private Boolean alarmTimeagoUrgent;
    @SerializedName("alarmTimeagoUrgentMins")
    @Expose
    private Integer alarmTimeagoUrgentMins;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("scaleY")
    @Expose
    private String scaleY;
    @SerializedName("showPlugins")
    @Expose
    private String showPlugins;
    @SerializedName("showForecast")
    @Expose
    private String showForecast;
    @SerializedName("focusHours")
    @Expose
    private Integer focusHours;
    @SerializedName("heartbeat")
    @Expose
    private Integer heartbeat;
    @SerializedName("baseURL")
    @Expose
    private String baseURL;
    @SerializedName("authDefaultRoles")
    @Expose
    private String authDefaultRoles;
    @SerializedName("thresholds")
    @Expose
    private Thresholds thresholds;
    @SerializedName("DEFAULT_FEATURES")
    @Expose
    private List<String> dEFAULTFEATURES = null;
    @SerializedName("alarmTypes")
    @Expose
    private List<String> alarmTypes = null;
    @SerializedName("enable")
    @Expose
    private List<String> enable = null;

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Integer getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(Integer timeFormat) {
        this.timeFormat = timeFormat;
    }

    public Boolean getNightMode() {
        return nightMode;
    }

    public void setNightMode(Boolean nightMode) {
        this.nightMode = nightMode;
    }

    public String getEditMode() {
        return editMode;
    }

    public void setEditMode(String editMode) {
        this.editMode = editMode;
    }

    public String getShowRawbg() {
        return showRawbg;
    }

    public void setShowRawbg(String showRawbg) {
        this.showRawbg = showRawbg;
    }

    public String getCustomTitle() {
        return customTitle;
    }

    public void setCustomTitle(String customTitle) {
        this.customTitle = customTitle;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Boolean getAlarmUrgentHigh() {
        return alarmUrgentHigh;
    }

    public void setAlarmUrgentHigh(Boolean alarmUrgentHigh) {
        this.alarmUrgentHigh = alarmUrgentHigh;
    }

    public List<Integer> getAlarmUrgentHighMins() {
        return alarmUrgentHighMins;
    }

    public void setAlarmUrgentHighMins(List<Integer> alarmUrgentHighMins) {
        this.alarmUrgentHighMins = alarmUrgentHighMins;
    }

    public Boolean getAlarmHigh() {
        return alarmHigh;
    }

    public void setAlarmHigh(Boolean alarmHigh) {
        this.alarmHigh = alarmHigh;
    }

    public List<Integer> getAlarmHighMins() {
        return alarmHighMins;
    }

    public void setAlarmHighMins(List<Integer> alarmHighMins) {
        this.alarmHighMins = alarmHighMins;
    }

    public Boolean getAlarmLow() {
        return alarmLow;
    }

    public void setAlarmLow(Boolean alarmLow) {
        this.alarmLow = alarmLow;
    }

    public List<Integer> getAlarmLowMins() {
        return alarmLowMins;
    }

    public void setAlarmLowMins(List<Integer> alarmLowMins) {
        this.alarmLowMins = alarmLowMins;
    }

    public Boolean getAlarmUrgentLow() {
        return alarmUrgentLow;
    }

    public void setAlarmUrgentLow(Boolean alarmUrgentLow) {
        this.alarmUrgentLow = alarmUrgentLow;
    }

    public List<Integer> getAlarmUrgentLowMins() {
        return alarmUrgentLowMins;
    }

    public void setAlarmUrgentLowMins(List<Integer> alarmUrgentLowMins) {
        this.alarmUrgentLowMins = alarmUrgentLowMins;
    }

    public List<Integer> getAlarmUrgentMins() {
        return alarmUrgentMins;
    }

    public void setAlarmUrgentMins(List<Integer> alarmUrgentMins) {
        this.alarmUrgentMins = alarmUrgentMins;
    }

    public List<Integer> getAlarmWarnMins() {
        return alarmWarnMins;
    }

    public void setAlarmWarnMins(List<Integer> alarmWarnMins) {
        this.alarmWarnMins = alarmWarnMins;
    }

    public Boolean getAlarmTimeagoWarn() {
        return alarmTimeagoWarn;
    }

    public void setAlarmTimeagoWarn(Boolean alarmTimeagoWarn) {
        this.alarmTimeagoWarn = alarmTimeagoWarn;
    }

    public Integer getAlarmTimeagoWarnMins() {
        return alarmTimeagoWarnMins;
    }

    public void setAlarmTimeagoWarnMins(Integer alarmTimeagoWarnMins) {
        this.alarmTimeagoWarnMins = alarmTimeagoWarnMins;
    }

    public Boolean getAlarmTimeagoUrgent() {
        return alarmTimeagoUrgent;
    }

    public void setAlarmTimeagoUrgent(Boolean alarmTimeagoUrgent) {
        this.alarmTimeagoUrgent = alarmTimeagoUrgent;
    }

    public Integer getAlarmTimeagoUrgentMins() {
        return alarmTimeagoUrgentMins;
    }

    public void setAlarmTimeagoUrgentMins(Integer alarmTimeagoUrgentMins) {
        this.alarmTimeagoUrgentMins = alarmTimeagoUrgentMins;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getScaleY() {
        return scaleY;
    }

    public void setScaleY(String scaleY) {
        this.scaleY = scaleY;
    }

    public String getShowPlugins() {
        return showPlugins;
    }

    public void setShowPlugins(String showPlugins) {
        this.showPlugins = showPlugins;
    }

    public String getShowForecast() {
        return showForecast;
    }

    public void setShowForecast(String showForecast) {
        this.showForecast = showForecast;
    }

    public Integer getFocusHours() {
        return focusHours;
    }

    public void setFocusHours(Integer focusHours) {
        this.focusHours = focusHours;
    }

    public Integer getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(Integer heartbeat) {
        this.heartbeat = heartbeat;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getAuthDefaultRoles() {
        return authDefaultRoles;
    }

    public void setAuthDefaultRoles(String authDefaultRoles) {
        this.authDefaultRoles = authDefaultRoles;
    }

    public Thresholds getThresholds() {
        return thresholds;
    }

    public void setThresholds(Thresholds thresholds) {
        this.thresholds = thresholds;
    }

    public List<String> getDEFAULTFEATURES() {
        return dEFAULTFEATURES;
    }

    public void setDEFAULTFEATURES(List<String> dEFAULTFEATURES) {
        this.dEFAULTFEATURES = dEFAULTFEATURES;
    }

    public List<String> getAlarmTypes() {
        return alarmTypes;
    }

    public void setAlarmTypes(List<String> alarmTypes) {
        this.alarmTypes = alarmTypes;
    }

    public List<String> getEnable() {
        return enable;
    }

    public void setEnable(List<String> enable) {
        this.enable = enable;
    }

}

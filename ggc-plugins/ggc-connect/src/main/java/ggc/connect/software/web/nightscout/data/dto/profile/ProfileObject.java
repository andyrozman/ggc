
package ggc.connect.software.web.nightscout.data.dto.profile;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ggc.connect.software.web.nightscout.data.dto.ProfileEntry;

public class ProfileObject {

    @SerializedName("dia")
    @Expose
    private String dia;

    @SerializedName("carbratio")
    @Expose
    private List<ProfileEntry> carbRatio = null;

    @SerializedName("carbs_hr")
    @Expose
    private String carbsHr;

    @SerializedName("delay")
    @Expose
    private String delay;

    @SerializedName("sens")
    @Expose
    private List<ProfileEntry> sens = null;

    @SerializedName("timezone")
    @Expose
    private String timezone;

    @SerializedName("basal")
    @Expose
    private List<ProfileEntry> basal = null;

    @SerializedName("target_low")
    @Expose
    private List<ProfileEntry> targetLow = null;

    @SerializedName("target_high")
    @Expose
    private List<ProfileEntry> targetHigh = null;

    @SerializedName("startDate")
    @Expose
    private String startDate;

    @SerializedName("units")
    @Expose
    private String units;

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public List<ProfileEntry> getCarbRatio() {
        return carbRatio;
    }

    public void setCarbRatio(List<ProfileEntry> carbratio) {
        this.carbRatio = carbratio;
    }

    public String getCarbsHr() {
        return carbsHr;
    }

    public void setCarbsHr(String carbsHr) {
        this.carbsHr = carbsHr;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public List<ProfileEntry> getSens() {
        return sens;
    }

    public void setSens(List<ProfileEntry> sens) {
        this.sens = sens;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public List<ProfileEntry> getBasal() {
        return basal;
    }

    public void setBasal(List<ProfileEntry> basal) {
        this.basal = basal;
    }

    public List<ProfileEntry> getTargetLow() {
        return targetLow;
    }

    public void setTargetLow(List<ProfileEntry> targetLow) {
        this.targetLow = targetLow;
    }

    public List<ProfileEntry> getTargetHigh() {
        return targetHigh;
    }

    public void setTargetHigh(List<ProfileEntry> targetHigh) {
        this.targetHigh = targetHigh;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

}

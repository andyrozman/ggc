
package ggc.connect.software.web.nightscout.data.dto.treatment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Boluscalc {

    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("eventTime")
    @Expose
    private String eventTime;
    @SerializedName("targetBGLow")
    @Expose
    private Double targetBGLow;
    @SerializedName("targetBGHigh")
    @Expose
    private Double targetBGHigh;
    @SerializedName("isf")
    @Expose
    private Double isf;
    @SerializedName("ic")
    @Expose
    private Integer ic;
    @SerializedName("iob")
    @Expose
    private Double iob;
    @SerializedName("bolusiobused")
    @Expose
    private Boolean bolusiobused;
    @SerializedName("basaliobused")
    @Expose
    private Boolean basaliobused;
    @SerializedName("bg")
    @Expose
    private Double bg;
    @SerializedName("insulinbg")
    @Expose
    private Double insulinbg;
    @SerializedName("insulinbgused")
    @Expose
    private Boolean insulinbgused;
    @SerializedName("bgdiff")
    @Expose
    private Double bgdiff;
    @SerializedName("insulincarbs")
    @Expose
    private Double insulincarbs;
    @SerializedName("carbs")
    @Expose
    private Double carbs;
    @SerializedName("cob")
    @Expose
    private Double cob;
    @SerializedName("insulincob")
    @Expose
    private Double insulincob;
    @SerializedName("othercorrection")
    @Expose
    private Double othercorrection;
    @SerializedName("insulinsuperbolus")
    @Expose
    private Double insulinsuperbolus;
    @SerializedName("insulintrend")
    @Expose
    private Double insulintrend;
    @SerializedName("insulin")
    @Expose
    private Double insulin;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Double getTargetBGLow() {
        return targetBGLow;
    }

    public void setTargetBGLow(Double targetBGLow) {
        this.targetBGLow = targetBGLow;
    }

    public Double getTargetBGHigh() {
        return targetBGHigh;
    }

    public void setTargetBGHigh(Double targetBGHigh) {
        this.targetBGHigh = targetBGHigh;
    }

    public Double getIsf() {
        return isf;
    }

    public void setIsf(Double isf) {
        this.isf = isf;
    }

    public Integer getIc() {
        return ic;
    }

    public void setIc(Integer ic) {
        this.ic = ic;
    }

    public Double getIob() {
        return iob;
    }

    public void setIob(Double iob) {
        this.iob = iob;
    }

    public Boolean getBolusiobused() {
        return bolusiobused;
    }

    public void setBolusiobused(Boolean bolusiobused) {
        this.bolusiobused = bolusiobused;
    }

    public Boolean getBasaliobused() {
        return basaliobused;
    }

    public void setBasaliobused(Boolean basaliobused) {
        this.basaliobused = basaliobused;
    }

    public Double getBg() {
        return bg;
    }

    public void setBg(Double bg) {
        this.bg = bg;
    }

    public Double getInsulinbg() {
        return insulinbg;
    }

    public void setInsulinbg(Double insulinbg) {
        this.insulinbg = insulinbg;
    }

    public Boolean getInsulinbgused() {
        return insulinbgused;
    }

    public void setInsulinbgused(Boolean insulinbgused) {
        this.insulinbgused = insulinbgused;
    }

    public Double getBgdiff() {
        return bgdiff;
    }

    public void setBgdiff(Double bgdiff) {
        this.bgdiff = bgdiff;
    }

    public Double getInsulincarbs() {
        return insulincarbs;
    }

    public void setInsulincarbs(Double insulincarbs) {
        this.insulincarbs = insulincarbs;
    }

    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }

    public Double getCob() {
        return cob;
    }

    public void setCob(Double cob) {
        this.cob = cob;
    }

    public Double getInsulincob() {
        return insulincob;
    }

    public void setInsulincob(Double insulincob) {
        this.insulincob = insulincob;
    }

    public Double getOthercorrection() {
        return othercorrection;
    }

    public void setOthercorrection(Double othercorrection) {
        this.othercorrection = othercorrection;
    }

    public Double getInsulinsuperbolus() {
        return insulinsuperbolus;
    }

    public void setInsulinsuperbolus(Double insulinsuperbolus) {
        this.insulinsuperbolus = insulinsuperbolus;
    }

    public Double getInsulintrend() {
        return insulintrend;
    }

    public void setInsulintrend(Double insulintrend) {
        this.insulintrend = insulintrend;
    }

    public Double getInsulin() {
        return insulin;
    }

    public void setInsulin(Double insulin) {
        this.insulin = insulin;
    }

}

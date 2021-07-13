
package ggc.connect.software.web.nightscout.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ggc.connect.software.web.nightscout.data.defs.EntryType;

public class Entry {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("device")
    @Expose
    private String device;
    @SerializedName("date")
    @Expose
    private Long date;
    @SerializedName("dateString")
    @Expose
    private String dateString;
    @SerializedName("sgv")
    @Expose
    private Integer sgv;
    @SerializedName("delta")
    @Expose
    private Double delta;
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("filtered")
    @Expose
    private Double filtered;
    @SerializedName("unfiltered")
    @Expose
    private Double unfiltered;
    @SerializedName("rssi")
    @Expose
    private Integer rssi;
    @SerializedName("noise")
    @Expose
    private Integer noise;
    @SerializedName("sysTime")
    @Expose
    private String sysTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public Integer getSgv() {
        return sgv;
    }

    public void setSgv(Integer sgv) {
        this.sgv = sgv;
    }

    public Double getDelta() {
        return delta;
    }

    public void setDelta(Double delta) {
        this.delta = delta;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getType() {
        return type;
    }


    public EntryType getEntryType() {
        return EntryType.getByCode(this.type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getFiltered() {
        return filtered;
    }

    public void setFiltered(Double filtered) {
        this.filtered = filtered;
    }

    public Double getUnfiltered() {
        return unfiltered;
    }

    public void setUnfiltered(Double unfiltered) {
        this.unfiltered = unfiltered;
    }

    public Integer getRssi() {
        return rssi;
    }

    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }

    public Integer getNoise() {
        return noise;
    }

    public void setNoise(Integer noise) {
        this.noise = noise;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    @Override
    public String toString() {
        return "Entry [" +
                "id='" + id + '\'' +
                ", device='" + device + '\'' +
                ", date=" + date +
                ", dateString='" + dateString + '\'' +
                ", sgv=" + sgv +
                ", delta=" + delta +
                ", direction='" + direction + '\'' +
                ", type='" + type + '\'' +
                ", filtered=" + filtered +
                ", unfiltered=" + unfiltered +
                ", rssi=" + rssi +
                ", noise=" + noise +
                ", sysTime='" + sysTime + '\'' +
                ']';
    }
}

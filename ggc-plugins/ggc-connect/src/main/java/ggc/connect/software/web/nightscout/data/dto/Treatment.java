
package ggc.connect.software.web.nightscout.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ggc.connect.software.web.nightscout.data.dto.treatment.Boluscalc;

public class Treatment {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("eventType")
    @Expose
    private String eventType;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("percent")
    @Expose
    private Integer percent;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("enteredBy")
    @Expose
    private String enteredBy;
    @SerializedName("NSCLIENT_ID")
    @Expose
    private Long nSCLIENTID;

    @SerializedName("carbs")
    @Expose
    private Object carbs;

    @SerializedName("insulin")
    @Expose
    private Double insulin;

    @SerializedName("notes")
    @Expose
    private String notes;

    @SerializedName("date")
    @Expose
    private Long date;

    @SerializedName("isSMB")
    @Expose
    private Boolean isSMB;
    @SerializedName("pumpId")
    @Expose
    private Long pumpId;

    @SerializedName("glucose")
    @Expose
    private Double glucose;
    @SerializedName("glucoseType")
    @Expose
    private String glucoseType; // "Sensor"
    @SerializedName("boluscalc")
    @Expose
    private Boluscalc boluscalc;
    @SerializedName("isAnnouncement")
    @Expose
    private Boolean isAnnouncement;
    @SerializedName("units")
    @Expose
    private String units; // "mmol"

    @SerializedName("profile")
    @Expose
    private String profile;

    @SerializedName("profileJson")
    @Expose
    private String profileJson;

    @SerializedName("profilePlugin")
    @Expose
    private String profilePlugin;


    @SerializedName("timestamp")
    @Expose
    private Long timestamp;

    @SerializedName("uuid")
    @Expose
    private String uuid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        this.enteredBy = enteredBy;
    }

    public Long getNSCLIENTID() {
        return nSCLIENTID;
    }

    public void setNSCLIENTID(Long nSCLIENTID) {
        this.nSCLIENTID = nSCLIENTID;
    }

    public Object getCarbs() {
        return carbs;
    }

    public void setCarbs(Object carbs) {
        this.carbs = carbs;
    }

    public Double getInsulin() {
        return insulin;
    }

    public void setInsulin(Double insulin) {
        this.insulin = insulin;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Boolean getIsSMB() {
        return isSMB;
    }

    public void setIsSMB(Boolean isSMB) {
        this.isSMB = isSMB;
    }

    public Long getPumpId() {
        return pumpId;
    }

    public void setPumpId(Long pumpId) {
        this.pumpId = pumpId;
    }

    public Double getGlucose() {
        return glucose;
    }

    public void setGlucose(Double glucose) {
        this.glucose = glucose;
    }

    public String getGlucoseType() {
        return glucoseType;
    }

    public void setGlucoseType(String glucoseType) {
        this.glucoseType = glucoseType;
    }

    public Boluscalc getBoluscalc() {
        return boluscalc;
    }

    public void setBoluscalc(Boluscalc boluscalc) {
        this.boluscalc = boluscalc;
    }

    public Boolean getIsAnnouncement() {
        return isAnnouncement;
    }

    public void setIsAnnouncement(Boolean isAnnouncement) {
        this.isAnnouncement = isAnnouncement;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }



    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private void addToStringBuilder(String displayString, Object object, StringBuilder stringBuilder)
    {
        if (object!=null)
            stringBuilder.append(displayString);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("Treatment [");

        addToStringBuilder("id='" + id + "'", id, sb);
        addToStringBuilder(", eventType='" + eventType + "'", eventType, sb);
        addToStringBuilder(", duration=" + duration, duration, sb);
        addToStringBuilder(", percent=" + percent , percent, sb);
        addToStringBuilder(", createdAt='" + createdAt+"'", createdAt, sb);
        addToStringBuilder(", enteredBy='" + enteredBy+"'", enteredBy, sb);
        addToStringBuilder(", nSCLIENTID=" + nSCLIENTID, nSCLIENTID, sb);
        addToStringBuilder(", carbs=" + carbs, carbs, sb);
        addToStringBuilder(", insulin=" + insulin, insulin, sb);
        addToStringBuilder(", notes='" + notes + "'", notes, sb);
        addToStringBuilder(", date=" + date, date, sb);
        addToStringBuilder(", isSMB=" + isSMB, isSMB, sb);
        addToStringBuilder(", pumpId=" + pumpId, pumpId, sb);
        addToStringBuilder(", glucose=" + glucose, glucose, sb);
        addToStringBuilder(", glucoseType='" + glucoseType + "'", glucoseType, sb);
        addToStringBuilder(", boluscalc=" + boluscalc, boluscalc, sb);
        addToStringBuilder(", isAnnouncement=" + isAnnouncement, isAnnouncement, sb);
        addToStringBuilder(", units='" + units + "'", units, sb);
        addToStringBuilder(", profile='" + profile + "'", profile, sb);
        addToStringBuilder(", profileJson='" + profileJson+ '\'', profileJson, sb);
        addToStringBuilder(", profilePlugin='" + profilePlugin + "'", profilePlugin, sb);
        addToStringBuilder(", timestamp=" + timestamp, timestamp, sb);
        addToStringBuilder(", uuid='" + uuid + "'", uuid, sb);
        sb.append(']');

        return sb.toString();

    }

}

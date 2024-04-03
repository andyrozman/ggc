
package ggc.connect.software.web.nightscout.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileEntry {

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("timeAsSeconds")
    @Expose
    private String timeAsSeconds;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTimeAsSeconds() {
        return timeAsSeconds;
    }

    public void setTimeAsSeconds(String timeAsSeconds) {
        this.timeAsSeconds = timeAsSeconds;
    }

}

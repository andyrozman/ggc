
package ggc.connect.software.web.nightscout.data.dto.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtendedSettings {

    @SerializedName("pump")
    @Expose
    private Pump pump;
    @SerializedName("openaps")
    @Expose
    private Openaps openaps;
    @SerializedName("basal")
    @Expose
    private Basal basal;
    @SerializedName("devicestatus")
    @Expose
    private Devicestatus devicestatus;

    public Pump getPump() {
        return pump;
    }

    public void setPump(Pump pump) {
        this.pump = pump;
    }

    public Openaps getOpenaps() {
        return openaps;
    }

    public void setOpenaps(Openaps openaps) {
        this.openaps = openaps;
    }

    public Basal getBasal() {
        return basal;
    }

    public void setBasal(Basal basal) {
        this.basal = basal;
    }

    public Devicestatus getDevicestatus() {
        return devicestatus;
    }

    public void setDevicestatus(Devicestatus devicestatus) {
        this.devicestatus = devicestatus;
    }

}


package ggc.connect.software.web.nightscout.data.dto.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thresholds {

    @SerializedName("bgHigh")
    @Expose
    private Integer bgHigh;
    @SerializedName("bgTargetTop")
    @Expose
    private Integer bgTargetTop;
    @SerializedName("bgTargetBottom")
    @Expose
    private Integer bgTargetBottom;
    @SerializedName("bgLow")
    @Expose
    private Integer bgLow;

    public Integer getBgHigh() {
        return bgHigh;
    }

    public void setBgHigh(Integer bgHigh) {
        this.bgHigh = bgHigh;
    }

    public Integer getBgTargetTop() {
        return bgTargetTop;
    }

    public void setBgTargetTop(Integer bgTargetTop) {
        this.bgTargetTop = bgTargetTop;
    }

    public Integer getBgTargetBottom() {
        return bgTargetBottom;
    }

    public void setBgTargetBottom(Integer bgTargetBottom) {
        this.bgTargetBottom = bgTargetBottom;
    }

    public Integer getBgLow() {
        return bgLow;
    }

    public void setBgLow(Integer bgLow) {
        this.bgLow = bgLow;
    }

}

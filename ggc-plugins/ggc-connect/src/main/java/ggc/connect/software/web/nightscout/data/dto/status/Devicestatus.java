
package ggc.connect.software.web.nightscout.data.dto.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Devicestatus {

    @SerializedName("advanced")
    @Expose
    private Boolean advanced;

    public Boolean getAdvanced() {
        return advanced;
    }

    public void setAdvanced(Boolean advanced) {
        this.advanced = advanced;
    }

}

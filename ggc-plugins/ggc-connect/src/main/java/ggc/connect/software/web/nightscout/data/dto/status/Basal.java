
package ggc.connect.software.web.nightscout.data.dto.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Basal {

    @SerializedName("render")
    @Expose
    private String render;

    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
    }

}

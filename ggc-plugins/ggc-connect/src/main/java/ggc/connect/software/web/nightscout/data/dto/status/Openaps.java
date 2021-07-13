
package ggc.connect.software.web.nightscout.data.dto.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Openaps {

    @SerializedName("fields")
    @Expose
    private String fields;

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

}

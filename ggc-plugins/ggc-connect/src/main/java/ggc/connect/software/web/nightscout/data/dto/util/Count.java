package ggc.connect.software.web.nightscout.data.dto.util;

/**
 * Created by andy on 3/15/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Count {

    @SerializedName("_id")
    @Expose
    private Object id;
    @SerializedName("count")
    @Expose
    private Integer count;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
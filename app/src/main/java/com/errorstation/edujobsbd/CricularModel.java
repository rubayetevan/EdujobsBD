
package com.errorstation.edujobsbd;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CricularModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("circular")
    @Expose
    private List<Circular> circular = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<Circular> getCircular() {
        return circular;
    }

    public void setCircular(List<Circular> circular) {
        this.circular = circular;
    }

}


package bgu.spl.mics.json.output;

import java.util.List;

import bgu.spl.mics.application.passiveObjects.Report;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Diaryoutput {

    @SerializedName("reports")
    @Expose
    private List<Report> reports = null;
    @SerializedName("total")
    @Expose
    private Integer total;

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public Diaryoutput withReports(List<Report> reports) {
        this.reports = reports;
        return this;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Diaryoutput withTotal(Integer total) {
        this.total = total;
        return this;
    }

}

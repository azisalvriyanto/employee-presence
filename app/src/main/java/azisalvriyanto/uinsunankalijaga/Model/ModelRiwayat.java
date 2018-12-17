package azisalvriyanto.uinsunankalijaga.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelRiwayat {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("hasil")
    @Expose
    private ArrayList<ModelRiwayatData> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<ModelRiwayatData> getData() {
        return data;
    }

    public void setData(ArrayList<ModelRiwayatData> data) {
        this.data = data;
    }
}
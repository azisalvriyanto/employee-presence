package azisalvriyanto.uinsunankalijaga.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRiwayatDetail {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("hasil")
    @Expose
    private ModelRiwayatDetailData data;

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public ModelRiwayatDetailData getData() {
        return data;
    }
    public void setData(ModelRiwayatDetailData data) {
        this.data = data;
    }
}

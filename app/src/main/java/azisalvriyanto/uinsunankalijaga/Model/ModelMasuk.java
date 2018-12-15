package azisalvriyanto.uinsunankalijaga.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelMasuk {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private ModelMasukData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ModelMasukData getData() {
        return data;
    }

    public void setData(ModelMasukData data) {
        this.data = data;
    }
}

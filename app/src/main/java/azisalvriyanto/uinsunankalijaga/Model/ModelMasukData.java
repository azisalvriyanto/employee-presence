package azisalvriyanto.uinsunankalijaga.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelMasukData {
    @SerializedName("nip")
    @Expose
    private String nip;

    public String getNIP() {
        return nip;
    }

    public void getNIP(String nip) {
        this.nip = nip;
    }
}

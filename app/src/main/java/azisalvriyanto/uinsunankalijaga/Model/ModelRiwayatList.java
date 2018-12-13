package azisalvriyanto.uinsunankalijaga.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelRiwayatList {
    @SerializedName("data")
    @Expose
    private ArrayList<ModelRiwayat> riwayat = null;

    public ArrayList<ModelRiwayat> getRiwayat() {
        return riwayat;
    }

    public void setRiwayat(ArrayList<ModelRiwayat> riwayat) {
        this.riwayat = riwayat;
    }
}

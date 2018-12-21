package azisalvriyanto.uinsunankalijaga.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRiwayatData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("jenis")
    @Expose
    private String jenis;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;
    @SerializedName("waktu")
    @Expose
    private String waktu;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @SerializedName("berkas")
    @Expose
    private String berkas;
    @SerializedName("status")
    @Expose
    private String status;

    public String getID() {
        return id;
    }
    public void setID(String id) {
        this.id = id;
    }

    public String getJenis() {
        return jenis;
    }
    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getTanggal() {
        return tanggal;
    }
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }
    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getKeterangan() {
        return keterangan;
    }
    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getBerkas() {
        return berkas;
    }
    public void setBerkas(String berkas) {
        this.berkas = berkas;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
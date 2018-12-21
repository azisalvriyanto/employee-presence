package azisalvriyanto.uinsunankalijaga.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPenggunaData {
    @SerializedName("nip")
    @Expose
    private String nip;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("foto")
    @Expose
    private String foto;
    @SerializedName("lahir_tempat")
    @Expose
    private String lahirTempat;
    @SerializedName("lahir_tanggal")
    @Expose
    private String lahirTanggal;
    @SerializedName("jkelamin")
    @Expose
    private String jkelamin;
    @SerializedName("fakultas")
    @Expose
    private String fakultas;
    @SerializedName("golongan")
    @Expose
    private String golongan;
    @SerializedName("email")
    @Expose
    private String email;

    public String getNIP() {
        return nip;
    }
    public void setNIP(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getLahirTempat() {
        return lahirTempat;
    }
    public void getLahirTempat(String lahirTempat) {
        this.lahirTempat = lahirTempat;
    }

    public String getLahirTanggal() {
        return lahirTanggal;
    }
    public void setLahirTanggal(String lahirTanggal) {
        this.lahirTanggal = lahirTanggal;
    }

    public String getJKelamin() {
        return jkelamin;
    }
    public void setJKelamin(String jkelamin) {
        this.jkelamin = jkelamin;
    }

    public String getFakultas() {
        return fakultas;
    }
    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public String getGolongn() {
        return golongan;
    }
    public void setGolongn(String golongan) {
        this.golongan = golongan;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}

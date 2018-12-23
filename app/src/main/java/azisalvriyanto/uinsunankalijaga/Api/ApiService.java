package azisalvriyanto.uinsunankalijaga.Api;

import azisalvriyanto.uinsunankalijaga.Model.ModelMasuk;
import azisalvriyanto.uinsunankalijaga.Model.ModelPengguna;
import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayat;
import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayatTambah;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @FormUrlEncoded
    @POST("masuk.php")
    Call<ModelMasuk> masuk(
            @Field("nip") String username,
            @Field("password") String password
    );

    @Multipart
    @POST("riwayat_tambah.php")
    Call<ModelRiwayatTambah> absensi(
            @Query("jenis") String jenis,
            @Query("nip") String username,
            @Query("tanggal") String tanggal,
            @Query("waktu") String waktu,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Part MultipartBody.Part berkasFile,
            @Part("file") RequestBody berkasName,
            @Query("keterangan") String keterangan
    );
    //@Part MultipartBody.Part berkas,

    @FormUrlEncoded
    @POST("riwayat.php")
    Call<ModelRiwayat> riwayat(@Field("nip") String username);

    @FormUrlEncoded
    @POST("pengguna.php")
    Call<ModelPengguna> pengguna(@Field("nip") String username);
}

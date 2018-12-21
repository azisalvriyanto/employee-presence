package azisalvriyanto.uinsunankalijaga.Api;

import azisalvriyanto.uinsunankalijaga.Model.ModelMasuk;
import azisalvriyanto.uinsunankalijaga.Model.ModelPengguna;
import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayat;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @FormUrlEncoded
    @POST("masuk.php")
    Call<ModelMasuk> masuk(
            @Field("nip") String username,
            @Field("password") String password
    );

    @Multipart
    @POST("riwayat_tambah.php")
    Call<ResponseBody> absensi(
            @Field("jenis") String jenis,
            @Field("nip") String username,
            @Field("tanggal") String tanggal,
            @Field("waktu") String waktu,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Part MultipartBody.Part berkas,
            @Field("keterangan") String keterangan
    );

    @FormUrlEncoded
    @POST("riwayat.php")
    Call<ModelRiwayat> riwayat(@Field("nip") String username);

    @FormUrlEncoded
    @POST("pengguna.php")
    Call<ModelPengguna> pengguna(@Field("nip") String username);
}

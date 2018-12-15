package azisalvriyanto.uinsunankalijaga.Api;

import azisalvriyanto.uinsunankalijaga.Model.ModelMasuk;
import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayatList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("aa_riwayat.php")
    Call<ModelRiwayatList> getMyJSON();

    @FormUrlEncoded
    @POST("aa_akun.php")
    Call<ModelMasuk> masuk(@Field("nip") String username, @Field("password") String password);

    @POST("aa_akun.php")
    Call<ModelMasuk> keluar();
}

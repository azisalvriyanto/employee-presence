package azisalvriyanto.uinsunankalijaga.Api;

import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayatList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("aa_riwayat.php")
    Call<ModelRiwayatList> getMyJSON();
}

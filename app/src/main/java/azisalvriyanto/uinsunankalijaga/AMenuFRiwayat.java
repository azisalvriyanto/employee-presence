package azisalvriyanto.uinsunankalijaga;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import azisalvriyanto.uinsunankalijaga.Adapter.AdapterRiwayat;
import azisalvriyanto.uinsunankalijaga.Api.ApiClient;
import azisalvriyanto.uinsunankalijaga.Api.ApiService;
import azisalvriyanto.uinsunankalijaga.Model.ModelPengguna;
import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayat;
import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayatList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AMenuFRiwayat extends Fragment {
    public AMenuFRiwayat() {
        // Required empty public constructor
        //return Fragment();
    }

    private ArrayList<ModelRiwayat> riwayatList;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private AdapterRiwayat adapterRiwayat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.l_menu_friwayat, container, false);
        final String username = SaveSharedPreference.getNIP(getActivity().getApplicationContext());

        //DARI SINI
        progressDialog = new ProgressDialog(getActivity().getApplicationContext());
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Creating an object of our api interface
        Retrofit apiClient = ApiClient.getClient();
        ApiService apiService = apiClient.create(ApiService.class);
        Call<ModelRiwayatList> call = apiService.riwayat(username);
        call.enqueue(new Callback<ModelRiwayatList>() {
            @Override
            public void onResponse(Call<ModelRiwayatList> call, Response<ModelRiwayatList> response) {
                //Dismiss Dialog
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    riwayatList = response.body().getRiwayat();
                    recyclerView = (RecyclerView) recyclerView.findViewById(R.id.friwayat_layout);
                    adapterRiwayat = new AdapterRiwayat(riwayatList);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    recyclerView.setLayoutManager(eLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapterRiwayat);
                }
            }

            @Override
            public void onFailure(Call<ModelRiwayatList> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
        //SAMPE SINI

        return view;
    }
}
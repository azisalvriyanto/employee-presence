package azisalvriyanto.uinsunankalijaga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import azisalvriyanto.uinsunankalijaga.Adapter.AdapterRiwayat;
import azisalvriyanto.uinsunankalijaga.Api.ApiClient;
import azisalvriyanto.uinsunankalijaga.Api.ApiService;
import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayat;
import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayatData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class AMenuFRiwayat extends Fragment {
    public AMenuFRiwayat() {
        // Required empty public constructor
        //return Fragment();
    }

    private ArrayList<ModelRiwayatData> data;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private AdapterRiwayat adapterRiwayat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.l_menu_friwayat, container, false);
        final String username = SaveSharedPreference.getNIP(getActivity().getApplicationContext());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Creating an object of our api interface
        Retrofit apiClient      = ApiClient.getClient();
        ApiService apiService   = apiClient.create(ApiService.class);
        Call<ModelRiwayat> call = apiService.riwayat(username);
        call.enqueue(new Callback<ModelRiwayat>() {
            @Override
            public void onResponse(Call<ModelRiwayat> call, Response<ModelRiwayat> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body().getStatus().equals("sukses")) {
                            data = response.body().getData();
                            recyclerView = view.findViewById(R.id.friwayat_layout);
                            adapterRiwayat = new AdapterRiwayat(data);
                            RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                            recyclerView.setLayoutManager(eLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapterRiwayat);
                        }
                        else {
                            Toast.makeText(getActivity().getApplicationContext(), "Akun tidak ditemukan.", Toast.LENGTH_SHORT).show();

                            //keluar
                            SaveSharedPreference.setLoggedIn(getActivity().getApplicationContext(), false, "data_nip");
                            Intent intent = new Intent(getActivity().getApplicationContext(), AMasuk.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                        progressDialog.dismiss();
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Sambugan internet gagal.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "Kredensial tidak valid.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelRiwayat> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Sambugan internet gagal.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                Log.e("TAG", "=======onFailure: " + t.toString());
                t.printStackTrace();
            }
        });

        return view;
    }
}
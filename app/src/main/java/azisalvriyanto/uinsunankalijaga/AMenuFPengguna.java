package azisalvriyanto.uinsunankalijaga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import azisalvriyanto.uinsunankalijaga.Api.ApiClient;
import azisalvriyanto.uinsunankalijaga.Api.ApiService;
import azisalvriyanto.uinsunankalijaga.Model.ModelPengguna;
import azisalvriyanto.uinsunankalijaga.Model.ModelPenggunaPerbaharui;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class AMenuFPengguna extends Fragment {
    Button b_keluar, b_perbaharui;
    private ProgressDialog progressDialog;

    public AMenuFPengguna() {
        // Required empty public constructor
        //return Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.l_menu_fpengguna, container, false);
        final String username = SaveSharedPreference.getNIP(getActivity().getApplicationContext());

        //dialog
        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        //retrofit
        final Retrofit apiClient = ApiClient.getClient();
        final ApiService apiService = apiClient.create(ApiService.class);

        //editText
        final EditText data_nip_tv = view.findViewById(R.id.l_fpengguna_nip_et);
        final EditText data_nama_tv = view.findViewById(R.id.l_fpengguna_nama_et);
        final EditText data_lahirtempat_tv = view.findViewById(R.id.l_fpengguna_lahirtempat_et);
        final EditText data_lahirtanggal_tv = view.findViewById(R.id.l_fpengguna_lahirtanggal_et);
        final EditText data_jkelamin_tv = view.findViewById(R.id.l_fpengguna_jkelamin_et);
        final EditText data_fakultas_tv = view.findViewById(R.id.l_fpengguna_fakultas_et);
        final EditText data_golongan_tv = view.findViewById(R.id.l_fpengguna_golongan_et);
        final EditText data_email_tv = view.findViewById(R.id.l_fpengguna_email_et);

        Call<ModelPengguna> call = apiService.pengguna(username);
        call.enqueue(new Callback<ModelPengguna>() {
            @Override
            public void onResponse(Call<ModelPengguna> call, Response<ModelPengguna> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body().getStatus().equals("sukses")) {
                            ModelPengguna indo = response.body();
                            String data_nip = indo.getData().getNIP();
                            String data_nama = indo.getData().getNama();
                            String data_lahirtempat = indo.getData().getLahirTempat();
                            String data_lahirtanggal = indo.getData().getLahirTanggal();
                            String data_jkelamin = indo.getData().getJKelamin();
                            String data_fakultas = indo.getData().getFakultas();
                            String data_golongan = indo.getData().getGolongn();
                            String data_email = indo.getData().getEmail();

                            data_nip_tv.setText(data_nip);
                            data_nama_tv.setText(data_nama);
                            data_lahirtempat_tv.setText(data_lahirtempat);
                            data_lahirtanggal_tv.setText(data_lahirtanggal);
                            data_jkelamin_tv.setText(data_jkelamin);
                            data_fakultas_tv.setText(data_fakultas);
                            data_golongan_tv.setText(data_golongan);
                            data_email_tv.setText(data_email);

                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Akun tidak ditemukan.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Response gagal.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Credentials are not valid.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelPengguna> call, Throwable t) {
                Log.e("TAG", "=======onFailure: " + t.toString());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });

        b_keluar = view.findViewById(R.id.l_fpengguna_b_keluar);
        b_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set LoggedIn status to false
                SaveSharedPreference.setLoggedIn(getActivity().getApplicationContext(), false, "data_nip");
                Intent intent = new Intent(getActivity().getApplicationContext(), AMasuk.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        b_perbaharui = view.findViewById(R.id.l_fpengguna_b_perbaharui);
        b_perbaharui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Mohon tunggu...");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

                Call<ModelPenggunaPerbaharui> perbaharui = apiService.pengguna_perbaharui(
                        data_nip_tv.getText().toString(),
                        data_nama_tv.getText().toString(),
                        data_lahirtempat_tv.getText().toString(),
                        data_lahirtanggal_tv.getText().toString(),
                        data_jkelamin_tv.getText().toString(),
                        data_fakultas_tv.getText().toString(),
                        data_golongan_tv.getText().toString(),
                        data_email_tv.getText().toString()
                );

                perbaharui.enqueue(new Callback<ModelPenggunaPerbaharui>() {
                    @Override
                    public void onResponse(Call<ModelPenggunaPerbaharui> call, Response<ModelPenggunaPerbaharui> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                /*if (response.body().getStatus().equals("sukses")) {
                                    progressDialog.setMessage("Data berhasil diperbaharui.");
                                    progressDialog.setIndeterminate(false);
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();
                                    //getActivity().finish();
                                    //startActivity(getActivity().getApplicationContext());
                                }
                                else {
                                    progressDialog.setMessage("Data gagal diperbaharui.");
                                    progressDialog.setIndeterminate(false);
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();
                                }

                                progressDialog.dismiss();*/
                            } catch (Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(), "Response gagal.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Credentials are not valid.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelPenggunaPerbaharui> call, Throwable t) {
                        Log.e("TAG", "=======onFailure: " + t.toString());
                        t.printStackTrace();
                        progressDialog.dismiss();
                    }
                });
            }
        });

        return view;
    }
}

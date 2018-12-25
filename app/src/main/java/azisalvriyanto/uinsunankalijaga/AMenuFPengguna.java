package azisalvriyanto.uinsunankalijaga;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import azisalvriyanto.uinsunankalijaga.Api.ApiClient;
import azisalvriyanto.uinsunankalijaga.Api.ApiService;
import azisalvriyanto.uinsunankalijaga.Model.ModelPengguna;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static org.apache.commons.lang3.StringUtils.capitalize;

public class AMenuFPengguna extends Fragment {
    Button b_keluar, b_perbaharui;
    private ProgressDialog progressDialog;
    private Spinner data_fakultas_pilih;

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
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        //editText
        final TextView data_nip_tv          = view.findViewById(R.id.l_fpengguna_pengguna_nip);
        final TextView data_nama_tv         = view.findViewById(R.id.l_fpengguna_pengguna_nama);
        final TextView data_lahir_tv        = view.findViewById(R.id.l_fpengguna_lahir_value);
        final TextView data_jkelamin_tv     = view.findViewById(R.id.l_fpengguna_jkelamin_value);
        final TextView data_fakultas_tv     = view.findViewById(R.id.l_fpengguna_fakultas_value);
        final TextView data_golongan_tv     = view.findViewById(R.id.l_fpengguna_golongan_value);
        final TextView data_email_tv        = view.findViewById(R.id.l_fpengguna_email_value);

        //foto
        final ImageView imageView = view.findViewById(R.id.l_fpengguna_pengguna_foto);

        //retrofit
        final Retrofit apiClient = ApiClient.getClient();
        final ApiService apiService = apiClient.create(ApiService.class);

        Call<ModelPengguna> call = apiService.pengguna(username);
        call.enqueue(new Callback<ModelPengguna>() {
            @Override
            public void onResponse(Call<ModelPengguna> call, Response<ModelPengguna> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body().getStatus().equals("sukses")) {
                            ModelPengguna data = response.body();
                            String data_foto = data.getData().getFoto();
                            String data_foto_url = "http://presensi-pegawai.msftrailers.co.za/foto/"+data_foto;
                            loadImageFromUrl(data_foto_url, imageView);

                            String data_nip = data.getData().getNIP();
                            String data_nama = data.getData().getNama();
                            String data_lahirtempat = data.getData().getLahirTempat();
                            String data_lahirtanggal = data.getData().getLahirTanggal();
                            String data_jkelamin = data.getData().getJKelamin();
                            String data_fakultas = data.getData().getFakultas();
                            String data_golongan = data.getData().getGolongn();
                            String data_email = data.getData().getEmail();

                            data_nip_tv.setText(data_nip);
                            data_nama_tv.setText(data_nama);
                            data_lahir_tv.setText(data_lahirtempat+", "+data_lahirtanggal);
                            data_jkelamin_tv.setText(capitalize(data_jkelamin));
                            data_fakultas_tv.setText(data_fakultas);
                            data_golongan_tv.setText(data_golongan);
                            data_email_tv.setText(data_email.toLowerCase());
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
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Kredensial tidak valid.", Toast.LENGTH_SHORT).show();
                }
            }

            private void loadImageFromUrl(String imageUrl, ImageView image){
                Glide.with(getActivity().getApplicationContext())
                    .asBitmap()
                    .load(imageUrl)
                    .into(image);
            }

            @Override
            public void onFailure(Call<ModelPengguna> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Sambugan internet gagal.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                Log.e("TAG", "=======onFailure: " + t.toString());
                t.printStackTrace();
            }
        });

        b_keluar = view.findViewById(R.id.l_fpengguna_keluar_b);
        b_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.setTitle("Apakah anda yakin untuk keluar?");
                //dialogBuilder.setMessage("silahkan isi sesuai pesanan");
                dialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Set LoggedIn status to false
                        SaveSharedPreference.setLoggedIn(getActivity().getApplicationContext(), false, "data_nip");
                        Intent intent = new Intent(getActivity().getApplicationContext(), AMasuk.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                dialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogBuilder.create().show();
            }
        });

        return view;
    }
}

package azisalvriyanto.uinsunankalijaga;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import azisalvriyanto.uinsunankalijaga.Api.ApiClient;
import azisalvriyanto.uinsunankalijaga.Api.ApiService;
import azisalvriyanto.uinsunankalijaga.Model.ModelPengguna;
import azisalvriyanto.uinsunankalijaga.Model.ModelPenggunaData;
import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayatTambah;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AMenuFAbsensiIzinSakit extends Fragment {
    int REQUEST_CODE_DOC = 1;
    String latitude, longitude;
    Button file_pilih;
    File file;

    //retrofit
    final Retrofit apiClient = ApiClient.getClient();
    final ApiService apiService = apiClient.create(ApiService.class);

    public AMenuFAbsensiIzinSakit() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.l_menu_fabsensi_izinsakit, container, false);
        final String username = SaveSharedPreference.getNIP(getActivity());

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        final TextView tv_latitude    = view.findViewById(R.id.l_fabsensi_izinsakit_geolocation_latitude);
        final TextView tv_longitude   = view.findViewById(R.id.l_fabsensi_izinsakit_geolocation_longitude);
        final TextView tv_nip         = view.findViewById(R.id.l_fabsensi_izinsakit_pengguna_nip);
        final TextView tv_nama        = view.findViewById(R.id.l_fabsensi_izinsakit_pengguna_nama);
        final ImageView iv_fotop      = view.findViewById(R.id.l_fabsensi_izinsakit_pengguna_foto);

        Call<ModelPengguna> call = apiService.pengguna(username);
        call.enqueue(new Callback<ModelPengguna>() {
            @Override
            public void onResponse(Call<ModelPengguna> call, Response<ModelPengguna> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body().getStatus().equals("sukses")) {
                            ModelPenggunaData data = response.body().getData();

                            Glide.with(getActivity().getApplicationContext())
                                .asBitmap()
                                .load("http://presensi-pegawai.msftrailers.co.za/foto/"+data.getFoto())
                                .into(iv_fotop);
                            tv_nip.setText(data.getNIP());
                            tv_nama.setText(data.getNama());
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), response.body().getStatus()+"Akun tidak ditemukan.", Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
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
        
        //Spinner Jenis
        Spinner s_jenis = view.findViewById(R.id.l_fabsensi_izinsakit_jenis_pilih);
        //String[] jenis_array = { "Pilih salah satu jenis absensi.", "Sakit", "Izin" };
        /*ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, jenis_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jenis.setAdapter(adapter);*/

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude    = location.getLatitude()+"";
            longitude   = location.getLongitude()+"";

            final LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    latitude    = location.getLatitude()+"";
                    longitude   = location.getLongitude()+"";
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        }
        tv_latitude.setText(latitude);
        tv_longitude.setText(longitude);

        final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimetypes = {
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/msword",
                "application/pdf"
        };
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);

        file_pilih = view.findViewById(R.id.l_fabsensi_izinsakit_keterangan_berkas);
        file_pilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Intent.createChooser(intent,"Pilih berkas..."), REQUEST_CODE_DOC);
            }
        });

        Button b_kirim = view.findViewById(R.id.l_fabsensi_absensi_b_kirim);
        b_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            progressDialog.setMessage("Mohon tunggu...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

            Calendar kalender                   = Calendar.getInstance();
            SimpleDateFormat tanggal_format     = new SimpleDateFormat("dd/MM/yyy");
            SimpleDateFormat waktu_format       = new SimpleDateFormat("HH:MM:ss");
            final String tanggal                = tanggal_format.format(kalender.getTime());
            final String waktu                  = waktu_format.format(kalender.getTime());

            /*File kontol = new File("/storage/0403-0201/aaUNDUHAN/cetak166500365b61b23d1595b.pdf");
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), kontol);
            MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("berkas", kontol.getName(), requestBody);*/

            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("berkas", file.getName(), requestBody);

            EditText tv_keterangan = view.findViewById(R.id.l_fabsensi_izinsakit_keterangan_keterangan);
            String keterangan = tv_keterangan.getText().toString();

            Call<ModelRiwayatTambah> call = apiService.absensi("izin", username, tanggal, waktu, latitude, longitude, multipartBody, requestBody, keterangan);
            call.enqueue(new Callback<ModelRiwayatTambah>() {
                @Override
                public void onResponse(Call<ModelRiwayatTambah> call, Response<ModelRiwayatTambah> response) {
                    if (response.isSuccessful()) {
                        try {
                            if (response.body().getStatus().equals("sukses")) {
                                Toast.makeText(getActivity().getApplicationContext(), "SUKSES.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), response.body().getPesan()+" | Akun tidak ditemukan.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), "Response gagal.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Credentials are not valid.", Toast.LENGTH_SHORT).show();
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ModelRiwayatTambah> call, Throwable t) {
                    Log.e("TAG", "=======onFailure: " + t.toString());
                    t.printStackTrace();
                    progressDialog.dismiss();
                }
            });
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(resultcode, resultcode, data);
        Uri uri             = data.getData();
        String uriString    = uri.toString();
        //File file           = new File(uriString);
        File file           = new File("/storage/0403-0201/aaUNDUHAN/cetak166500365b61b23d1595b.pdf");
        String filePath     = file.getAbsolutePath();
        try {
            filePath = file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        file_pilih.setText(filePath);
    }
}

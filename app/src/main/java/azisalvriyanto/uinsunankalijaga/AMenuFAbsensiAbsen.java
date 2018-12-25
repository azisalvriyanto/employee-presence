package azisalvriyanto.uinsunankalijaga;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;

import azisalvriyanto.uinsunankalijaga.Api.ApiClient;
import azisalvriyanto.uinsunankalijaga.Api.ApiService;
import azisalvriyanto.uinsunankalijaga.Model.ModelPengguna;
import azisalvriyanto.uinsunankalijaga.Model.ModelRiwayatTambah;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class AMenuFAbsensiAbsen extends Fragment {
    ImageView iv_foto, iv_foto_ambil;
    private static final int requestcode = 1;
    String latitude, longitude;
    Uri uri;


    public AMenuFAbsensiAbsen() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.l_menu_fabsensi_absen, container, false);
        final String username = SaveSharedPreference.getNIP(getActivity().getApplicationContext());

        //Dialog
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        final TextView tv_latitude    = view.findViewById(R.id.l_friwayat_absen_geolocation_latitude);
        final TextView tv_longitude   = view.findViewById(R.id.l_friwayat_absen_geolocation_longitude);
        final TextView tv_nip         = view.findViewById(R.id.l_friwayat_absen_pengguna_nip);
        final TextView tv_nama        = view.findViewById(R.id.l_friwayat_absen_pengguna_nama);
        final ImageView iv_fotop      = view.findViewById(R.id.l_friwayat_absen_pengguna_foto);

        //foto
        iv_foto = view.findViewById(R.id.l_friwayat_absen_berkas_foto);

        //Retrofit
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
                            loadImageFromUrl(data_foto_url, iv_fotop);

                            String data_nip = data.getData().getNIP();
                            String data_nama = data.getData().getNama();

                            tv_nip.setText(data_nip);
                            tv_nama.setText(data_nama);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Akun tidak ditemukan.", Toast.LENGTH_SHORT).show();
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

        iv_foto_ambil = view.findViewById(R.id.l_friwayat_absen_berkas_foto_ambil);
        iv_foto_ambil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoCapturedIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCapturedIntent, requestcode);
            }
        });

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

        Button b_kirim = view.findViewById(R.id.l_friwayat_absen_b_kirim);
        b_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Mohon tunggu...");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

                File file = new File(getRealPathFromUri(getContext().getApplicationContext(), uri));
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("berkas", file.getName(), requestBody);

                Call<ModelRiwayatTambah> call = apiService.absensi("absen", username, latitude, longitude, multipartBody, requestBody, "");
                call.enqueue(new Callback<ModelRiwayatTambah>() {
                    @Override
                    public void onResponse(Call<ModelRiwayatTambah> call, Response<ModelRiwayatTambah> response) {
                        if (response.isSuccessful()) {
                            try {
                                if (response.body().getStatus().equals("sukses")) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Anda telah absen hari ini.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                }

                                progressDialog.dismiss();
                            } catch (Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(), "Sambugan internet gagal.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Kredensial tidak valid.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelRiwayatTambah> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), "Sambugan internet gagal.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        Log.e("TAG", "=======onFailure: " + t.toString());
                        t.printStackTrace();
                    }
                });
            }
        });

        return view;
    }

    public void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode, resultcode, data);
        if (this.requestcode == requestcode && resultcode == RESULT_OK){
            Bitmap bitmap =(Bitmap) data.getExtras().get("data");
            uri = getImageUri(getContext().getApplicationContext(), bitmap);
            iv_foto.setImageBitmap(bitmap);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



}

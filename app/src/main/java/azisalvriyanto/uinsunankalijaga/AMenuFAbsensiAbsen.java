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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
    private static final int MY_PERMISSIONS_REQUEST_CODE = 1337;
    private static final int CAMERA_REQUEST = 100;
    private static final int CAMERA_PERMISSION = 200;
    private static final int LOCATION_PERMISSION_CODE = 300;
    ImageView iv_foto, iv_foto_ambil;
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

        //permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermission();
        }

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
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                }
                else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
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
        if (RESULT_OK == resultcode) {
            if (CAMERA_REQUEST == requestcode){
                Bitmap bitmap =(Bitmap) data.getExtras().get("data");
                uri = getImageUri(getContext().getApplicationContext(), bitmap);
                iv_foto.setImageBitmap(bitmap);
            }
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

    protected void checkPermission(){
        if(
            ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            + ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            // Do something, when permissions not granted
            if(
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

            ) {
                // Show an alert dialog here with request explanation
                ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    MY_PERMISSIONS_REQUEST_CODE
                );
            }
        }
        else {
            //Do something, when permissions are already granted
            //Toast.makeText(getActivity(),"Permissions already granted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CODE: {
                // When request is cancelled, the results array are empty
                if(
                    (grantResults.length > 0) && (
                        grantResults[0]
                        + grantResults[1]
                    == PackageManager.PERMISSION_GRANTED )){
                    //Permissions are granted
                    //Toast.makeText(getActivity(),"Permissions granted.",Toast.LENGTH_SHORT).show();
                }
                else {
                    //Permissions are denied
                    //Toast.makeText(getActivity(),"Permissions denied.",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

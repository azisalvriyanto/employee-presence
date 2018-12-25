package azisalvriyanto.uinsunankalijaga;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
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
import java.lang.reflect.Array;
import java.lang.reflect.Method;

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

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStorageDirectory;
import static azisalvriyanto.uinsunankalijaga.BuildConfig.DEBUG;


public class AMenuFAbsensiIzinSakit extends Fragment {
    private static final int REQUEST_WRITE_STORAGE = 1;
    private static final int REQUEST_ACCESS_LOCATION = 1;
    int REQUEST_CODE_DOC = 1;
    String latitude, longitude;
    Button file_pilih;
    File file;

    //retrofit
    final Retrofit apiClient = ApiClient.getClient();
    final ApiService apiService = apiClient.create(ApiService.class);

    public static Context contextOfApplication;

    public AMenuFAbsensiIzinSakit() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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

        Boolean hasPermission = (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }

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
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);

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

        //new Intent(Intent.ACTION_OPEN_DOCUMENT);
        //Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimetypes = {
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/msword",
                "application/pdf",
                "image/*"
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

                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("berkas", file.getName(), requestBody);

                EditText tv_keterangan = view.findViewById(R.id.l_fabsensi_izinsakit_keterangan_keterangan);
                String keterangan = tv_keterangan.getText().toString();

                Call<ModelRiwayatTambah> call = apiService.absensi("izin", username, latitude, longitude, multipartBody, requestBody, keterangan);
                call.enqueue(new Callback<ModelRiwayatTambah>() {
                    @Override
                    public void onResponse(Call<ModelRiwayatTambah> call, Response<ModelRiwayatTambah> response) {
                        if (response.isSuccessful()) {
                            try {
                                if (response.body().getStatus().equals("sukses")) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Absensi telah diajukan.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), response.body().getPesan(), Toast.LENGTH_SHORT).show();
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
        if (requestcode == REQUEST_CODE_DOC && resultcode == RESULT_OK){
            Uri uri     = data.getData();
            file        = new File(getPath(getActivity(), uri));
            file_pilih.setText(file.getAbsolutePath());
        }

        super.onActivityResult(resultcode, resultcode, data);
    }

    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // LocalStorageProvider
            if (isLocalStorageDocument(uri)) {
                // The path is the id
                return DocumentsContract.getDocumentId(uri);
            }
            // ExternalStorageProvider
            else if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return getExternalStorageDirectory() + "/" + split[1];
                }
                else {
                   StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
                    try {
                        Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
                        Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
                        Method getUuid = storageVolumeClazz.getMethod("getUuid");
                        Method getState = storageVolumeClazz.getMethod("getState");
                        Method getPath = storageVolumeClazz.getMethod("getPath");
                        Method isPrimary = storageVolumeClazz.getMethod("isPrimary");
                        Method isEmulated = storageVolumeClazz.getMethod("isEmulated");

                        Object result = getVolumeList.invoke(mStorageManager);

                        final int length = Array.getLength(result);
                        for (int i = 0; i < length; i++) {
                            Object storageVolumeElement = Array.get(result, i);
                            //String uuid = (String) getUuid.invoke(storageVolumeElement);

                            final boolean mounted = Environment.MEDIA_MOUNTED.equals( getState.invoke(storageVolumeElement) )
                                    || Environment.MEDIA_MOUNTED_READ_ONLY.equals(getState.invoke(storageVolumeElement));

                            //if the media is not mounted, we need not get the volume details
                            if (!mounted) continue;
                            //Primary storage is already handled.
                            if ((Boolean)isPrimary.invoke(storageVolumeElement) && (Boolean)isEmulated.invoke(storageVolumeElement)) continue;

                            String uuid = (String) getUuid.invoke(storageVolumeElement);
                            if (uuid != null && uuid.equals(type)) {
                                return getPath.invoke(storageVolumeElement) + "/" +split[1];
                            }
                        }
                    }
                    catch (Exception e) {
                        Toast.makeText(context, "File gagal diambil.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static boolean isLocalStorageDocument(Uri uri) {
        return LocalStorageProvider.AUTHORITY.equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                if (DEBUG)
                    DatabaseUtils.dumpCursor(cursor);

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


}

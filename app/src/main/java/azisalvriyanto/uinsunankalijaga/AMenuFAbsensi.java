package azisalvriyanto.uinsunankalijaga;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AMenuFAbsensi extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_CODE = 1337;

    public AMenuFAbsensi() {
        // Required empty public constructor
        //return Fragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.l_menu_fabsensi, container, false);

        Button l_b_absen        = (Button) view.findViewById(R.id.l_friwayat_absensi_b_absen);
        Button l_b_izinsakit    = (Button) view.findViewById(R.id.l_friwayat_absensi_b_izinsakit);

        //permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermission();
        }

        l_b_absen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFragment(new AMenuFAbsensiAbsen());
            }
        });

        l_b_izinsakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFragment(new AMenuFAbsensiIzinSakit());
            }
        });

        return view;
    }

    private Fragment initFragment(Fragment classFragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.l_menu_flayout, classFragment);
        transaction.commit();
        return classFragment;
    }

    protected void checkPermission(){
        if(
            ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
            + ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            + ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            // Do something, when permissions not granted
            if(
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

                ) {
                // Show an alert dialog here with request explanation
                ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{
                            Manifest.permission.CAMERA,
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
                        + grantResults[2]
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
